//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// (C) Copyright 2018 Modeling Value Group B.V. (http://modelingvalue.org)                                             ~
//                                                                                                                     ~
// Licensed under the GNU Lesser General Public License v3.0 (the "License"). You may not use this file except in      ~
// compliance with the License. You may obtain a copy of the License at: https://choosealicense.com/licenses/lgpl-3.0  ~
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on ~
// an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the  ~
// specific language governing permissions and limitations under the License.                                          ~
//                                                                                                                     ~
// Contributors:                                                                                                       ~
//     Wim Bast, Carel Bast, Tom Brus, Arjan Kok, Ronald Krijgsheld                                                    ~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package org.modelingvalue.dclare.mps;

import java.util.Objects;
import java.util.stream.Collectors;

import org.jetbrains.mps.openapi.language.SLanguage;
import org.jetbrains.mps.openapi.module.SRepository;
import org.modelingvalue.collections.Collection;
import org.modelingvalue.collections.ContainingCollection;
import org.modelingvalue.collections.Entry;
import org.modelingvalue.collections.Map;
import org.modelingvalue.collections.QualifiedSet;
import org.modelingvalue.collections.Set;
import org.modelingvalue.collections.util.Context;
import org.modelingvalue.collections.util.Pair;
import org.modelingvalue.dclare.mps.DAttribute.DObservedAttribute;
import org.modelingvalue.transactions.AbstractLeaf;
import org.modelingvalue.transactions.Compound;
import org.modelingvalue.transactions.EmptyMandatoryException;
import org.modelingvalue.transactions.Leaf;
import org.modelingvalue.transactions.Observed;
import org.modelingvalue.transactions.Observer;
import org.modelingvalue.transactions.Priority;
import org.modelingvalue.transactions.Root;
import org.modelingvalue.transactions.Setable;
import org.modelingvalue.transactions.Slot;
import org.modelingvalue.transactions.State;
import org.modelingvalue.transactions.StopObserverException;
import org.modelingvalue.transactions.TooManyChangesException;
import org.modelingvalue.transactions.TooManySubscriptionsException;

@SuppressWarnings("rawtypes")
public abstract class DObject<O> {

    private static final Set<DMessageType>                              MESSAGE_TYPES = Collection.of(DMessageType.values()).toSet();

    private static final QualifiedSet<Pair<DFeature, String>, DMessage> MESSAGES_SET  = QualifiedSet.of(m -> Pair.of(m.feature(), m.id()));

    private static final class DRuleObserver extends Observer {

        private DRuleObserver(DRule rule, Compound parent, Runnable action) {
            super(rule, parent, () -> ((DRuleObserver) AbstractLeaf.getCurrent()).run(action), Priority.mid);
        }

        private void run(Runnable action) {
            try {
                if (firstTime()) {
                    object().removeMessages(rule(), DMessageType.error, "EXCEPTION");
                }
                if (parent.equals(DObject.TRANSACTION.get(object())) && object().isActive()) {
                    action.run();
                }
            } catch (NullPointerException e) {
                if (EMPTY_ATTRIBUTE.get()) {
                    throw new EmptyMandatoryException();
                } else {
                    object().addMessage(rule(), DMessageType.error, "EXCEPTION", e);
                    throw new StopObserverException(e);
                }
            } catch (IndexOutOfBoundsException e) {
                if (COLLECTION_ATTRIBUTE.get()) {
                    throw new EmptyMandatoryException();
                } else {
                    object().addMessage(rule(), DMessageType.error, "EXCEPTION", e);
                    throw new StopObserverException(e);
                }
            } catch (Throwable e) {
                object().addMessage(rule(), DMessageType.error, "EXCEPTION", e);
                throw new StopObserverException(e);
            } finally {
                COLLECTION_ATTRIBUTE.set(false);
                EMPTY_ATTRIBUTE.set(false);
            }
        }

        @Override
        protected void countChanges(Observed observed) {
            if (observed instanceof DObserved && !((DObserved) observed).isSynthetic()) {
                super.countChanges(observed);
            }
        }

        private DObject object() {
            return (DObject) parent.getId();
        }

        private DRule rule() {
            return (DRule) getId();
        }

        @Override
        protected void observe(Root root, Set<Slot> sets, Set<Slot> gets) {
            try {
                super.observe(root, sets, gets);
            } catch (TooManySubscriptionsException e) {
                object().addMessage(rule(), DMessageType.warning, "TOO_MANY_SUBSCRIPTIONS", e);
            }
        }

        @Override
        protected void checkTooManyChanges(State pre, Root root, Set<Slot> sets, Set<Slot> gets) {
            if (root.isDebugging()) {
                sets = sets.filter(s -> s.observed() instanceof DObserved && !((DObserved) s.observed()).isSynthetic()).toSet();
                gets = gets.filter(s -> s.observed() instanceof DObserved && !((DObserved) s.observed()).isSynthetic()).toSet();
            }
            try {
                super.checkTooManyChanges(pre, root, sets, gets);
            } catch (TooManyChangesException e) {
                object().addMessage(rule(), "CONFLICTING_RULES", e);
                throw new StopObserverException(e);
            }
        }

    }

    protected final static class NonCheckingObserver extends Observer {

        protected NonCheckingObserver(Object id, Compound parent, Runnable action, Priority prio) {
            super(id, parent, action, prio);
        }

        @Override
        protected void countChanges(Observed observed) {
        }

    }

    protected static final Context<Boolean>                                                                    EMPTY_ATTRIBUTE      = Context.of(false);
    protected static final Context<Boolean>                                                                    COLLECTION_ATTRIBUTE = Context.of(false);

    public static final Setable<DObject, DType>                                                                TYPE                 = Observed.of("TYPE", new DType("<DUMMY_TYPE>") {
                                                                                                                                        @Override
                                                                                                                                        public Set<DRule> getRules(Set<IRuleSet> ruleSets) {
                                                                                                                                            return Set.of();
                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public Set<DAttribute> getAttributes(Set<IRuleSet> ruleSets) {
                                                                                                                                            return Set.of();
                                                                                                                                        }

                                                                                                                                        @Override
                                                                                                                                        public Set<SLanguage> getLanguages() {
                                                                                                                                            return Set.of();
                                                                                                                                        }
                                                                                                                                    });

    public static final Observed<DObject, DObserved>                                                           CONTAINING           = DObserved.of("CONTAINING", null, false, false, false, false, (o, b, a, f) -> {
                                                                                                                                    }, null);

    @SuppressWarnings("unchecked")
    public static final Setable<DObject, DObject>                                                              PARENT               = DObserved.of("PARENT", null, false, false, false, false, (o, b, a, f) -> {
                                                                                                                                    }, (tx, o, b, a) -> {
                                                                                                                                        if (b != null && a != null) {
                                                                                                                                            DObserved cont = CONTAINING.get(o);
                                                                                                                                            if (cont != null) {
                                                                                                                                                cont.remove(b, o);
                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                    }, null);

    public static final Setable<DObject, Set<? extends DObject>>                                               CHILDREN             = Observed.of("CHILDREN", Set.of(), (tx, o, b, a) -> {
                                                                                                                                        Setable.<Set<? extends DObject>, DObject> diff(Set.of(), b, a,                   //
                                                                                                                                                e -> e.activate(o, tx.parent()), e -> e.deactivate(o, tx.parent()));
                                                                                                                                    });
    @SuppressWarnings("unchecked")
    public static final Setable<DObject, Compound>                                                             TRANSACTION          = Observed.of("TRANSACTION", null, (tx, o, b, a) -> {
                                                                                                                                        if (a != null) {
                                                                                                                                            o.rule(CHILDREN, a,                                                          //
                                                                                                                                                    () -> CHILDREN.set(o, o.getAllChildren().toSet()),                   //
                                                                                                                                                    () -> CHILDREN.set(o, Set.of()), Priority.high);
                                                                                                                                        }
                                                                                                                                    });

    public static final Setable<DObject, DObjectState>                                                         STATE                = Observed.of("STATE", DObjectState.orphan);

    private static final Setable<DObject, Set<Observer>>                                                       RULE_INSTANCES       = Setable.of("RULE_INSTANCES", Set.of(), (tx, o, b, a) -> {
                                                                                                                                        Setable.<Set<Observer>, Observer> diff(Set.of(), b, a, Leaf::trigger, tx::clear);
                                                                                                                                    });

    protected static final Setable<DObject, Map<DMessageType, Set<? extends DObject>>>                         MESSAGE_CHILDREN     = Observed.of("MESSAGE_CHILDREN", MESSAGE_TYPES.toMap(t -> Entry.of(t, Set.of())));

    protected static final Setable<DObject, Map<DMessageType, Boolean>>                                        MESSAGES_OR_CHILDREN = Observed.of("MESSAGES_OR_CHILDREN", MESSAGE_TYPES.toMap(t -> Entry.of(t, false)));

    protected static final Setable<DObject, Map<DMessageType, QualifiedSet<Pair<DFeature, String>, DMessage>>> MESSAGES             = Observed.of("MESSAGES", Map.of());

    public static DClareMPS dClareMPS() {
        return DClareMPS.instance();
    }

    protected final O original;

    protected DObject(O original) {
        this.original = original;
    }

    public O original() {
        return original;
    }

    @Override
    public int hashCode() {
        return original.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof DObject) {
            DObject other = (DObject) obj;
            return original.equals(other.original);
        } else {
            return false;
        }
    }

    public Set<? extends DObject> getMessageChildren(DMessageType type) {
        Set<? extends DObject> set = MESSAGE_CHILDREN.get(this).get(type);
        return set != null ? set : Set.of();
    }

    public QualifiedSet<Pair<DFeature, String>, DMessage> getMessages(DMessageType type) {
        QualifiedSet<Pair<DFeature, String>, DMessage> set = MESSAGES.get(this).get(type);
        return set != null ? set : MESSAGES_SET;
    }

    public abstract boolean isReadOnly();

    @Override
    public String toString() {
        return original().toString();
    }

    public java.util.List<DAttribute> getAttributes() {
        return TYPE.get(this).getAttributes().collect(Collectors.toList());
    }

    public java.util.List<DAttribute> getNonSyntheticAttributes() {
        return TYPE.get(this).getNonSyntheticAttributes().collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    public ContainingCollection<? extends DNode> getAllChildren() {
        return getContained().addAll((ContainingCollection) getChildren());
    }

    protected abstract ContainingCollection<? extends DObject> getChildren();

    @SuppressWarnings("unchecked")
    private ContainingCollection<? extends DObject> getContained() {
        return TYPE.get(this).getAttributes().filter(DAttribute::isComposite).flatMap(a -> getDObjectSet(a.get(this))).toSet();
    }

    @SuppressWarnings("unchecked")
    public static Set<DObject<?>> getDObjectSet(Object v) {
        if (v instanceof Collection) {
            return ((Collection) v).toSet();
        } else if (v instanceof java.util.Collection) {
            return Set.of((java.util.Collection) v);
        } else {
            return v == null ? Set.of() : Set.of((DObject) v);
        }
    }

    protected void init(DClareMPS dClareMPS) {
    }

    @SuppressWarnings("unchecked")
    protected Compound activate(DObject parent, Compound parentTx) {
        DClareMPS dClareMPS = dClareMPS();
        dClareMPS.read(() -> init(dClareMPS));
        Compound tx = Compound.of(this, parentTx);
        TRANSACTION.set(this, tx);
        STATE.set(this, DObjectState.contained);
        rule(TYPE, tx, () -> {
            TYPE.set(this, getType());
        }, () -> TYPE.set(this, TYPE.getDefault()), Priority.high);
        rule("<CONSTANT_CONTAINMENT>", tx, () -> {
            TYPE.get(this).getAttributes().filter(DAttribute::isConstant).filter(DAttribute::isComposite).forEach(cc -> cc.get(this));
        }, Priority.high);
        rule(RULE_INSTANCES, tx, () -> {
            RULE_INSTANCES.set(this, TYPE.get(this).getRules().map(r -> rule(tx, r)).toSet());
        }, () -> RULE_INSTANCES.set(this, Set.of()), Priority.high);
        if (!(this instanceof DRepository || this instanceof DModule)) {
            rule(STATE, tx, () -> {
                STATE.set(this, PARENT.get(this).state());
            }, Priority.high);
        }
        rule(MESSAGES_OR_CHILDREN, tx, () -> {
            MESSAGES_OR_CHILDREN.set(this, MESSAGE_TYPES.toMap(t -> Entry.of(t, !getMessages(t).isEmpty() || !getMessageChildren(t).isEmpty())));
        }, () -> MESSAGES_OR_CHILDREN.set(this, Map.of()), Priority.low);
        rule(MESSAGE_CHILDREN, tx, () -> {
            Set<? extends DObject> children = CHILDREN.get(this);
            MESSAGE_CHILDREN.set(this, MESSAGE_TYPES.toMap(t -> Entry.of(t, children.filter(c -> MESSAGES_OR_CHILDREN.get(c).get(t)).toSet())));
        }, () -> MESSAGE_CHILDREN.set(this, Map.of()), Priority.low);
        rule("<EMPTY_MANDATORY>", tx, () -> {
            for (DAttribute attr : TYPE.get(this).getAttributes()) {
                if (attr instanceof DObservedAttribute && attr.isMandatory() && !attr.isSynthetic()) {
                    if (attr.get(this) == null) {
                        addMessage(attr, DMessageType.warning, "MANDATORY", "Mandatory attribute " + attr + " of " + this + " is null");
                    } else {
                        removeMessages(attr, DMessageType.warning, "MANDATORY");
                    }
                }
            }
        }, () -> MESSAGES.set(this, MESSAGES.getDefault()), Priority.low);
        rule("<REFERENCED_ORPHAN>", tx, () -> {
            for (DAttribute attr : TYPE.get(this).getAttributes()) {
                if (attr instanceof DObservedAttribute && !attr.isComposite() && !attr.isSynthetic()) {
                    Set<DObject> orphans = Collection.of(attr.getIterable(this)).filter(DObject.class).filter(o -> {
                        return !((DObject) o).isReadOnly() && !isInOtherRepository((DObject) o) && !((DObject) o).isOwned();
                    }).toSet();
                    if (!orphans.isEmpty()) {
                        addMessage(attr, DMessageType.warning, "ORPHAN", "Non-composite attribute " + attr + " of " + this + " references orphans " + orphans.toString().substring(3));
                    } else {
                        removeMessages(attr, DMessageType.warning, "ORPHAN");
                    }
                }
            }
        }, () -> MESSAGES.set(this, MESSAGES.getDefault()), Priority.low);
        return tx;

    }

    protected abstract SRepository getOriginalRepository();

    private boolean isInOtherRepository(DObject o) {
        if (isReadOnly()) {
            return true;
        } else {
            SRepository r = o.getOriginalRepository();
            return r != null && !r.equals(dClareMPS().getRepository().original());
        }
    }

    protected void exit(DClareMPS dClareMPS) {
    }

    protected DObjectState state() {
        return STATE.get(this);
    }

    protected void deactivate(DObject parent, Compound parentTx) {
        Compound tx = TRANSACTION.get(this);
        if (tx != null && Objects.equals(parentTx, tx.parent())) {
            TRANSACTION.set(this, null);
        }
        STATE.set(this, DObjectState.orphan);
        dClareMPS().read(() -> exit(dClareMPS()));
    }

    @SuppressWarnings("unchecked")
    public <T> T ancestor(Class<T> cls) {
        for (DObject p = PARENT.get(this); p != null; p = PARENT.get(p)) {
            if (cls.isInstance(p)) {
                return (T) p;
            }
        }
        return null;
    }

    protected boolean isOwned() {
        return PARENT.get(this) != null && STATE.get(this) != DObjectState.orphan;
    }

    protected boolean isActive() {
        return PARENT.get(this) != null && STATE.get(this) == DObjectState.active;
    }

    protected abstract DType getType();

    @SuppressWarnings("unchecked")
    private Observer rule(Compound parent, DRule rule) {
        return new DRuleObserver(rule, parent, () -> rule.run(DObject.this));
    }

    protected void addMessage(DFeature feature, String id, TooManyChangesException tmce) {
        DMessage message = new DMessage(this, feature, DMessageType.error, id, "Conflicting rules, running " + feature + " changes=" + tmce.getNrOfChanges());
        tmce.getLast().trace(message, (m, r) -> {
            m.addSubMessage(new DMessage(((DRuleObserver) r.observer()).object(), ((DRuleObserver) r.observer()).rule(), DMessageType.error, id, //
                    "run: " + ((DRuleObserver) r.observer()).object() + "." + ((DRuleObserver) r.observer()).rule() + " nr: " + r.nrOfChanges()));
        }, (m, r, s) -> {
            m.addSubMessage(new DMessage((DObject) s.object(), (DObserved) s.observed(), DMessageType.error, id, //
                    "read: " + s.object() + "." + s.observed() + "=" + r.read().get(s)));
        }, (m, w, s) -> {
            m.subMessages().last().addSubMessage(new DMessage((DObject) s.object(), (DObserved) s.observed(), DMessageType.error, id, //
                    "write: " + s.object() + "." + s.observed() + "=" + w.written().get(s)));
        }, m -> m.subMessages().last(), tmce.getObserver().root().maxNrOfChanges());
        addMessage(message);
    }

    protected void addMessage(DFeature feature, DMessageType type, String id, Throwable content) {
        DMessage message = new DMessage(this, feature, type, id, content);
        for (StackTraceElement ste : content.getStackTrace()) {
            message.addSubMessage(new DMessage(this, feature, type, id, ste));
        }
        addMessage(message);
    }

    protected void addMessage(DFeature feature, DMessageType type, String id, Object content) {
        addMessage(new DMessage(this, feature, type, id, content));
    }

    private void addMessage(DMessage message) {
        MESSAGES.set(this, (m, s) -> m.put(message.type(), s), getMessages(message.type()).add(message));
    }

    protected void removeMessages(DFeature feature, DMessageType type, String id) {
        MESSAGES.set(this, (m, s) -> m.put(type, s), getMessages(type).removeKey(Pair.of(feature, id)));
    }

    final public void rule(Object id, Compound tx, Runnable action) {
        makeRule(id, tx, action, () -> {
        }, Priority.mid).trigger();
    }

    final public void rule(Object id, Compound tx, Runnable action, Priority prio) {
        makeRule(id, tx, action, () -> {
        }, prio).trigger();
    }

    final public void rule(Object id, Compound tx, Runnable action, Runnable stop) {
        makeRule(id, tx, action, stop, Priority.mid).trigger();
    }

    final public void rule(Object id, Compound tx, Runnable action, Runnable stop, Priority prio) {
        makeRule(id, tx, action, stop, prio).trigger();
    }

    final private Observer makeRule(Object id, Compound tx, Runnable action, Runnable stop, Priority prio) {
        return new NonCheckingObserver(id, tx, () -> {
            if (tx.equals(DObject.TRANSACTION.get(this))) {
                if (prio == Priority.high ? isOwned() : isActive()) {
                    action.run();
                }
            } else {
                stop.run();
                throw new StopObserverException("Stopped");
            }
        }, prio);
    }

}
