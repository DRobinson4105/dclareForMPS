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

import java.util.function.Supplier;

import org.jetbrains.mps.openapi.language.SLanguage;
import org.modelingvalue.collections.Collection;
import org.modelingvalue.collections.Set;
import org.modelingvalue.collections.util.Pair;
import org.modelingvalue.dclare.*;
import org.modelingvalue.dclare.Construction.Reason;
import org.modelingvalue.dclare.mps.DAttribute.DIdentifyingAttribute;

@SuppressWarnings("rawtypes")
public abstract class DNewableObject<T extends DNewableObject, R, S> extends DIdentifiedObject implements Newable {

    private static final Constant<DNewableObject, Object>         ORIGINAL       = Constant.of("$ORIGINAL", null);

    protected static final Setable<DNewableObject, Set<Observed>> READ_OBSERVEDS = Setable.of("$READ_OBSERVEDS", Set.of());

    protected static final Action<DNewableObject>                 READ_DEEP      = Action.of("$READ_DEEP", DNewableObject::readDeep);

    @SuppressWarnings("unchecked")
    protected static final Set<Observer>                          OBSERVERS      = DObject.OBSERVERS;

    protected static final Set<Setable>                           SETABLES       = DObject.SETABLES.add(READ_OBSERVEDS);

    protected static <D extends DNewableObject> D quotationConstruct(IRuleSet ruleSet, String anonymousType, Object[] ctx, Supplier<D> supplier) {
        LeafTransaction tx = LeafTransaction.getCurrent();
        return tx.construct(new DQuotation(tx.mutable(), ruleSet, anonymousType, ctx), supplier);
    }

    protected static <D extends DNewableObject> D copyRootConstruct(IRuleSet ruleSet, String anonymousType, Object[] ctx, DNode copiedRoot, Supplier<D> supplier) {
        LeafTransaction tx = LeafTransaction.getCurrent();
        return tx.construct(new DCopy(tx.mutable(), copiedRoot, ruleSet, anonymousType, ctx), supplier);
    }

    protected static <D extends DNewableObject> D copyChildConstruct(DCopy root, DNode copiedChild, Supplier<D> supplier) {
        LeafTransaction tx = LeafTransaction.getCurrent();
        return tx.construct(new DCopy(copiedChild, root), supplier);
    }

    @SuppressWarnings("unchecked")
    protected static <D extends DNewableObject, I, S> D referenceConstruct(I ref, Supplier<D> supplier) {
        LeafTransaction tx = LeafTransaction.getCurrent();
        return tx.directConstruct(new DRead(ref), supplier);
    }

    @SuppressWarnings("unchecked")
    protected static <D extends DNewableObject, I, S> D originalConstruct(S original, I ref, Supplier<D> supplier) {
        D result = referenceConstruct(ref, supplier);
        ORIGINAL.force(result, original);
        return result;
    }

    protected DNewableObject(Object[] identity) {
        super(identity);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected <V> V get(DIdentifyingAttribute<?, V> attr) {
        Construction c = getQuotationConstruction(attr.anonymousType());
        if (c != null) {
            return (V) c.get(attr.index());
        } else {
            return null;
        }
    }

    @Override
    protected boolean isObsolete(String anonymousType) {
        return anonymousType != null && getQuotationConstruction(anonymousType) == null;
    }

    private Construction getQuotationConstruction(String anonymousType) {
        for (Construction c : dDerivedConstructions()) {
            if (c.reason() instanceof DQuotation && ((DQuotation) c.reason()).anonymousType() == anonymousType) {
                return c;
            }
        }
        return null;
    }

    @Override
    protected void read(DClareMPS dClareMPS) {
    }

    @SuppressWarnings("unchecked")
    protected final R reference() {
        DRead<R> rc = referenceReason();
        return rc != null ? rc.reference() : null;
    }

    @SuppressWarnings("unchecked")
    protected final DRead<R> referenceReason() {
        Construction cons = dDirectConstruction();
        return cons != null && cons.reason() instanceof DRead ? (DRead) cons.reason() : null;
    }

    protected Collection<Reason> deriveReasons() {
        return dDerivedConstructions().map(Construction::reason);
    }

    public Set<String> getAnonymousTypes() {
        return deriveReasons().filter(DQuotation.class).map(DDerive::anonymousType).notNull().toSet();
    }

    public IAspect getCopyAspect() {
        return deriveReasons().filter(DCopy.class).map(DDerive::aspect).findFirst().orElse(null);
    }

    public Set<SLanguage> getAnonymousLanguages() {
        return deriveReasons().filter(DQuotation.class).map(DDerive::language).notNull().toSet();
    }

    @Override
    protected boolean isRead() {
        return tryOriginal() != null;
    }

    private void parentFromMPS(DObject parent) {
        if (!DNewableObject.READ_OBSERVEDS.set(this, s -> s.add(Mutable.D_PARENT_CONTAINING)).contains(Mutable.D_PARENT_CONTAINING)) {
            if (dClareMPS().getConfig().isTraceActivation()) {
                System.err.println(DclareTrace.getLineStart("ACTIVATE") + this);
            }
            setParentFromMPS(parent);
        }
    }

    protected abstract void setParentFromMPS(DObject parent);

    @SuppressWarnings("unchecked")
    public final S tryOriginal() {
        S sObject = ORIGINAL.isSet(this) ? (S) ORIGINAL.get(this) : null;
        if (sObject == null) {
            R ref = reference();
            sObject = ref != null ? dClareMPS().read(() -> resolve(ref)) : null;
            if (sObject != null) {
                ORIGINAL.force(this, sObject);
            }
        }
        return sObject;
    }

    @SuppressWarnings("unchecked")
    protected final S original() {
        R ref = reference();
        S sObject = ref != null ? dClareMPS().read(() -> resolve(ref)) : null;
        if (sObject == null) {
            sObject = ORIGINAL.isSet(this) ? (S) ORIGINAL.get(this) : null;
            if (sObject == null) {
                sObject = create(ref);
            }
        }
        return sObject;
    }

    protected void init(S sObject) {
        originalConstruct(sObject, reference(sObject), () -> this);
        init(dClareMPS(), sObject);
    }

    @Override
    protected void init(DClareMPS dClareMPS) {
        super.init(dClareMPS);
        S original = tryOriginal();
        if (original != null) {
            init(dClareMPS, original);
        }
    }

    @Override
    protected void exit(DClareMPS dClareMPS) {
        super.exit(dClareMPS);
        S original = tryOriginal();
        if (original != null) {
            exit(dClareMPS, original);
        }
    }

    @Override
    public final void dActivate() {
        Newable.super.dActivate();
        start(dClareMPS());
    }

    @Override
    public final void dDeactivate() {
        Newable.super.dDeactivate();
        stop(dClareMPS());
    }

    @SuppressWarnings("unchecked")
    protected void triggerInitRead(DObserved observed) {
        if (!DNewableObject.READ_OBSERVEDS.get(this).contains(observed)) {
            observed.initReadAction().trigger(this);
        }
    }

    protected void triggerSetParentFromMPS() {
        if (!DNode.READ_OBSERVEDS.get(this).contains(Mutable.D_PARENT_CONTAINING)) {
            Action.<DObject> of(Pair.of("SET_PARENT_FROM_MPS", this), this::parentFromMPS).trigger(originalParent());
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void readObserved(Set<Observed> read, DObserved dObserved) {
        if (read.contains(dObserved)) {
            dObserved.readAction().trigger(this);
            if (dObserved.containment()) {
                for (Object child : dObserved.collection(dObserved.fromMPS(this))) {
                    if (child instanceof DNewableObject) {
                        READ_DEEP.trigger((DNewableObject) child);
                    }
                }
            }
        } else if (dObserved.containment()) {
            Set<Object> set = dObserved.collection(dObserved.fromMPS(this)).toSet();
            for (Object child : dObserved.getCollection(this)) {
                if (child instanceof DNewableObject && set.contains(child)) {
                    READ_DEEP.trigger((DNewableObject) child);
                } else {
                    dObserved.remove(this, child);
                }
            }
        }
    }

    protected abstract void readDeep();

    protected abstract void init(DClareMPS dClareMPS, S original);

    protected abstract void exit(DClareMPS dClareMPS, S original);

    protected abstract R reference(S read);

    protected abstract S resolve(R ref);

    protected abstract S create(R ref);

    protected abstract DObject originalParent();

}
