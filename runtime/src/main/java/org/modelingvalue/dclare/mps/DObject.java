//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// (C) Copyright 2018-2022 Modeling Value Group B.V. (http://modelingvalue.org)                                        ~
//                                                                                                                     ~
// Licensed under the GNU Lesser General Public License v3.0 (the 'License'). You may not use this file except in      ~
// compliance with the License. You may obtain a copy of the License at: https://choosealicense.com/licenses/lgpl-3.0  ~
// Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on ~
// an 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the  ~
// specific language governing permissions and limitations under the License.                                          ~
//                                                                                                                     ~
// Maintainers:                                                                                                        ~
//     Wim Bast, Tom Brus, Ronald Krijgsheld                                                                           ~
// Contributors:                                                                                                       ~
//     Arjan Kok, Carel Bast                                                                                           ~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package org.modelingvalue.dclare.mps;

import static org.modelingvalue.dclare.SetableModifier.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.mps.openapi.language.SLanguage;
import org.modelingvalue.collections.Collection;
import org.modelingvalue.collections.Set;
import org.modelingvalue.collections.util.Pair;
import org.modelingvalue.dclare.*;

import jetbrains.mps.errors.item.IssueKindReportItem;

@SuppressWarnings({"rawtypes", "unused"})
public abstract class DObject implements Mutable {

    private static final Runnable                                                      DUMMY_RUNNABLE            = () -> {
                                                                                                                 };

    private static final DObjectType<String>                                           DUMMY_TYPE                = new DObjectType<>("<DUMMY_TYPE>") {
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

                                                                                                                     @Override
                                                                                                                     protected Collection<Observer> observers() {
                                                                                                                         return Set.of(DObject.TYPE_RULE);
                                                                                                                     }

                                                                                                                     @Override
                                                                                                                     protected Collection<Setable> setables() {
                                                                                                                         return Set.of(DObject.TYPE);
                                                                                                                     }

                                                                                                                 };

    public static final DObserved<DObject, DObjectType<?>>                             TYPE                      = DObserved.of("$TYPE", DUMMY_TYPE, DObject::getType, null, plumbing);

    protected static final Observer<DObject>                                           TYPE_RULE                 = observer(TYPE, DObject::getType);

    protected static final Observed<DObject, DAttribute>                               CONTAINING_ATTRIBUTE      = Observed.of("$CONTAINING_ATTRIBUTE", null, plumbing);

    protected static final Observer<DObject>                                           CONTAINING_ATTRIBUTE_RULE = observer(CONTAINING_ATTRIBUTE, o -> {
                                                                                                                     Pair<Mutable, Setable<Mutable, ?>> pc = o.dParentContaining();
                                                                                                                     return pc == null || pc.a() instanceof DClareMPS ? null :                                                       //
                                                                                                                             pc.b() instanceof DAttribute ? (DAttribute) pc.b() : CONTAINING_ATTRIBUTE.get((DObject) pc.a());
                                                                                                                 });

    protected static final DObserved<DObject, Set<Pair<DObject, IssueKindReportItem>>> MPS_ISSUES                = DObserved.of("$MPS_ISSUES", Set.of(), null, null, (tx, o, pre, post) -> {
                                                                                                                     if (o instanceof DNode) {
                                                                                                                         DNode root = ((DNode) o).getContainingRoot();
                                                                                                                         if (root != null) {
                                                                                                                             Setable.<Set<Pair<DObject, IssueKindReportItem>>, Pair<DObject, IssueKindReportItem>> diff(pre, post,   //
                                                                                                                                     a -> DNode.ALL_MPS_ISSUES.set(root, Set::add, a),                                               //
                                                                                                                                     r -> DNode.ALL_MPS_ISSUES.set(root, Set::remove, r));
                                                                                                                         }
                                                                                                                     } else {
                                                                                                                         DRepository repos = DClareMPS.instance().getRepository();
                                                                                                                         Setable.<Set<Pair<DObject, IssueKindReportItem>>, Pair<DObject, IssueKindReportItem>> diff(pre, post,       //
                                                                                                                                 a -> DRepository.ALL_MPS_ISSUES.set(repos, Set::add, a.b()),                                        //
                                                                                                                                 r -> DRepository.ALL_MPS_ISSUES.set(repos, Set::remove, r.b()));
                                                                                                                     }
                                                                                                                 });

    protected static final DObserved<DObject, Set<DIssue>>                             DCLARE_ISSUES             = DObserved.of("$DCLARE_ISSUES", Set.of(), null, (dObject, pre, post) -> {
                                                                                                                 }, containment);

    protected static final DObserved<DObject, Boolean>                                 CONTAINED                 = DObserved.of("$CONTAINED", Boolean.FALSE, null, (dObject, pre, post) -> {
                                                                                                                 }, plumbing);

    protected static final Setable<DObject, Set<Observed>>                             READ_OBSERVEDS            = Setable.of("$READ_OBSERVEDS", Set.of(), plumbing, preserved);

    protected static final Set<Observer>                                               OBSERVERS                 = Set.of(TYPE_RULE, CONTAINING_ATTRIBUTE_RULE);

    protected static final Set<Setable>                                                SETABLES                  = Set.of(TYPE, MPS_ISSUES, DCLARE_ISSUES, CONTAINING_ATTRIBUTE, CONTAINED, READ_OBSERVEDS);

    public static DClareMPS dClareMPS() {
        return DClareMPS.instance();
    }

    public boolean isOwned() {
        return dParent() != null;
    }

    public java.util.List<DAttribute> getAttributes() {
        return TYPE.get(this).getAttributes().collect(Collectors.toList());
    }

    public java.util.Set<? extends IssueKindReportItem> getIssues() {
        return MPS_ISSUES.get(this).map(Pair::b).collect(Collectors.toSet());
    }

    public java.util.List<DAttribute> getNonSyntheticAttributes() {
        return TYPE.get(this).getNonSyntheticAttributes().collect(Collectors.toList());
    }

    @Override
    public DObjectType<?> dClass() {
        return TYPE.get(this);
    }

    @SuppressWarnings("unchecked")
    public Collection<DObject> getAllChildren() {
        return (Collection<DObject>) dChildren();
    }

    @SuppressWarnings("unchecked")
    public static Set<DObject> getDObjectSet(Object v) {
        if (v instanceof Collection) {
            return ((Collection) v).toSet();
        } else if (v instanceof java.util.Collection) {
            return Set.of((java.util.Collection) v);
        } else {
            return v == null ? Set.of() : Set.of((DObject) v);
        }
    }

    @Override
    public void dActivate() {
        Mutable.super.dActivate();
        start(dClareMPS());
    }

    @Override
    public void dDeactivate() {
        Mutable.super.dDeactivate();
        stop(dClareMPS());
    }

    protected abstract void read(DClareMPS dClareMPS);

    protected void init(DClareMPS dClareMPS) {
    }

    protected void exit(DClareMPS dClareMPS) {
    }

    protected final void start(DClareMPS dClareMPS) {
        init(dClareMPS);
        read(dClareMPS);
    }

    protected void stop(DClareMPS dClareMPS) {
        exit(dClareMPS);
    }

    public DObject dObjectParent() {
        Object parent = dParent();
        return parent instanceof DObject ? (DObject) parent : null;
    }

    protected abstract DObjectType<?> getType();

    public static <O extends DObject> NonCheckingObserver<O> observer(String id, Consumer<O> action, LeafModifier... modifiers) {
        return NonCheckingObserver.of(id, action, modifiers);
    }

    public static <O extends DObject, V> NonCheckingObserver<O> observer(Setable<O, V> setable, Function<O, V> value, LeafModifier... modifiers) {
        return NonCheckingObserver.of(setable, value, modifiers);
    }

    public boolean isDclareOnly() {
        return !DObject.READ_OBSERVEDS.get(this).contains(Mutable.D_PARENT_CONTAINING) || CONTAINING_ATTRIBUTE.get(this) != null;
    }

    @Override
    public boolean dCheckConsistency() {
        return !isExternal();
    }

    @Override
    public ConstantState dMemoization(AbstractDerivationTransaction tx) {
        return isExternal() ? tx.universeTransaction().constantState() : Mutable.super.dMemoization(tx);
    }

    public abstract boolean isExternal();

    protected boolean isObsolete(String anonymousType) {
        return false;
    }

    protected boolean readConstant() {
        return isExternal() || Constant.DERIVED.get() != null || LeafTransaction.getCurrent() instanceof DerivationTransaction;
    }

    protected boolean isObserving() {
        return LeafTransaction.getCurrent() instanceof ObserverTransaction && ObserverTransaction.OBSERVE.get();
    }

    protected abstract boolean isRead();

    @SuppressWarnings("unchecked")
    @Override
    public Pair<Mutable, Setable<Mutable, ?>> dParentContaining() {
        if (isRead()) {
            if (readConstant()) {
                return (Pair) readParent();
            } else if (isObserving()) {
                initAncestors(DObject.READ_OBSERVEDS.get(this));
            }
        }
        return Mutable.super.dParentContaining();
    }

    @Override
    public Pair<Mutable, Setable<Mutable, ?>> dSetParentContaining(Pair<Mutable, Setable<Mutable, ?>> pc) {
        if (!isRead() && isObserving()) {
            DObject.READ_OBSERVEDS.add(this, Mutable.D_PARENT_CONTAINING);
        }
        return Mutable.super.dSetParentContaining(pc);
    }

    protected abstract Pair<DObject, DObserved<DObject, ?>> readParent();

    @SuppressWarnings("rawtypes")
    protected void initAncestors(Set<Observed> reads) {
        if (!reads.contains(Mutable.D_PARENT_CONTAINING)) {
            Pair<DObject, DObserved<DObject, ?>> parent = readParent();
            parent.a().initAncestors(DObject.READ_OBSERVEDS.get(parent.a()));
            parent.b().triggerInitParent(parent.a(), this);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends Observer<?>> dAllObservers() {
        return !isExternal() ? Mutable.super.dAllObservers() : (Collection) dClass().observers();
    }

    protected boolean isActive() {
        return !isExternal() && DObject.READ_OBSERVEDS.get(this).contains(Mutable.D_PARENT_CONTAINING);
    }

}
