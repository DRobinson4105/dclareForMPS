//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//  (C) Copyright 2018-2024 Modeling Value Group B.V. (http://modelingvalue.org)                                         ~
//                                                                                                                       ~
//  Licensed under the GNU Lesser General Public License v3.0 (the 'License'). You may not use this file except in       ~
//  compliance with the License. You may obtain a copy of the License at: https://choosealicense.com/licenses/lgpl-3.0   ~
//  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on  ~
//  an 'AS IS' BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the   ~
//  specific language governing permissions and limitations under the License.                                           ~
//                                                                                                                       ~
//  Maintainers:                                                                                                         ~
//      Wim Bast, Tom Brus                                                                                               ~
//                                                                                                                       ~
//  Contributors:                                                                                                        ~
//      Ronald Krijgsheld ✝, Arjan Kok, Carel Bast                                                                       ~
// --------------------------------------------------------------------------------------------------------------------- ~
//  In Memory of Ronald Krijgsheld, 1972 - 2023                                                                          ~
//      Ronald was suddenly and unexpectedly taken from us. He was not only our long-term colleague and team member      ~
//      but also our friend. "He will live on in many of the lines of code you see below."                               ~
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

package org.modelingvalue.dclare.mps;

import static org.modelingvalue.dclare.CoreSetableModifier.*;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jetbrains.mps.openapi.language.SLanguage;
import org.jetbrains.mps.openapi.model.SNode;
import org.modelingvalue.collections.Collection;
import org.modelingvalue.collections.Set;
import org.modelingvalue.collections.util.Pair;
import org.modelingvalue.collections.util.TriConsumer;
import org.modelingvalue.dclare.*;

import jetbrains.mps.errors.item.IssueKindReportItem;

@SuppressWarnings({"rawtypes", "unused"})
public abstract class DMutable implements DObject, Mutable {

    @SuppressWarnings("unchecked")
    protected static final DObserved<DMutable, Set<?>>                                   INFOS                     = DObserved.of("INFOS", Set.of(), (Function) null, (TriConsumer) null, plumbing);

    private static final Runnable                                                        DUMMY_RUNNABLE            = () -> {
                                                                                                                   };

    private static final DObjectType<String>                                             DUMMY_TYPE                = new DObjectType<>("<DUMMY_TYPE>") {
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
                                                                                                                           return Set.of();                                                                                              // Bootstrap
                                                                                                                       }

                                                                                                                       @Override
                                                                                                                       protected Collection<Setable> setables() {
                                                                                                                           return Set.of();                                                                                              // Bootstrap
                                                                                                                       }

                                                                                                                       @Override
                                                                                                                       public Set<INative> getNatives(Set<IRuleSet> ruleSets) {
                                                                                                                           return Set.of();
                                                                                                                       }

                                                                                                                   };

    protected static final Observed<DMutable, DObjectType<?>>                            TYPE                      = Observed.of("$TYPE", DUMMY_TYPE, (t, o, b, a) -> {
                                                                                                                       if (a.hasNatives()) {
                                                                                                                           DMutable.CONTAINED.set(o, true);
                                                                                                                       }
                                                                                                                       if (!a.getObservers().isEmpty() || !a.getAttributes().isEmpty()) {
                                                                                                                           o.readChildren();
                                                                                                                       }
                                                                                                                   }, plumbing);

    private static final Observer<DMutable>                                              TYPE_RULE                 = observer(TYPE, DMutable::getType);

    protected static final Observed<DMutable, DAttribute>                                CONTAINING_ATTRIBUTE      = Observed.of("$CONTAINING_ATTRIBUTE", null, plumbing);

    protected static final Observer<DMutable>                                            CONTAINING_ATTRIBUTE_RULE = observer(CONTAINING_ATTRIBUTE, o -> {
                                                                                                                       Pair<Mutable, Setable<Mutable, ?>> pc = o.dParentContaining();
                                                                                                                       return pc == null || pc.a() instanceof DClareMPS ? null :                                                         //
                                                                                                                               pc.b() instanceof DAttribute ? (DAttribute) pc.b() : CONTAINING_ATTRIBUTE.get((DMutable) pc.a());
                                                                                                                   });

    protected static final DObserved<DMutable, Set<Pair<DMutable, IssueKindReportItem>>> MPS_ISSUES                = DObserved.of("$MPS_ISSUES", Set.of(), null, null, (tx, o, pre, post) -> {
                                                                                                                       if (o instanceof DNode) {
                                                                                                                           DNode root = ((DNode) o).getContainingRoot();
                                                                                                                           if (root != null) {
                                                                                                                               Setable.<Set<Pair<DMutable, IssueKindReportItem>>, Pair<DMutable, IssueKindReportItem>> diff(pre, post,   //
                                                                                                                                       a -> DNode.ALL_MPS_ISSUES.set(root, Set::add, a),                                                 //
                                                                                                                                       r -> DNode.ALL_MPS_ISSUES.set(root, Set::remove, r));
                                                                                                                           }
                                                                                                                       } else {
                                                                                                                           DRepository repos = DClareMPS.instance().getRepository();
                                                                                                                           Setable.<Set<Pair<DMutable, IssueKindReportItem>>, Pair<DMutable, IssueKindReportItem>> diff(pre, post,       //
                                                                                                                                   a -> DRepository.ALL_MPS_ISSUES.set(repos, Set::add, a.b()),                                          //
                                                                                                                                   r -> DRepository.ALL_MPS_ISSUES.set(repos, Set::remove, r.b()));
                                                                                                                       }
                                                                                                                   });

    protected static final Setable<DMutable, Set<DIssue>>                                CONTAINED_DCLARE_ISSUES   = Setable.of("$CONTAINED_DCLARE_ISSUES", Set.of(), plumbing, containment);

    protected static final DObserved<DMutable, Set<DIssue>>                              DCLARE_ISSUES             = DObserved.of("$DCLARE_ISSUES", Set.of(), () -> DIssue.DOBJECT, null, (dObject, pre, post) -> {
                                                                                                                   }, plumbing);

    protected static final DObserved<DMutable, Boolean>                                  CONTAINED                 = DObserved.of("$CONTAINED", Boolean.FALSE, null, (dObject, pre, post) -> {
                                                                                                                       if (dObject instanceof DNode) {
                                                                                                                           SNode sNode = ((DNode) dObject).tryOriginal();
                                                                                                                           if (sNode != null) {
                                                                                                                               sNode.setProperty(DNode.PARENT_PROPERTY, "");
                                                                                                                               sNode.setProperty(DNode.PARENT_PROPERTY, null);
                                                                                                                           }
                                                                                                                       }
                                                                                                                   }, plumbing);

    protected static final Setable<DMutable, Set<Observed>>                              READ_OBSERVEDS            = Setable.of("$READ_OBSERVEDS", Set.of(), plumbing, preserved);

    protected static final Set<Observer>                                                 OBSERVERS                 = Set.of(TYPE_RULE, CONTAINING_ATTRIBUTE_RULE);

    protected static final Set<Setable>                                                  SETABLES                  = Set.of(TYPE, MPS_ISSUES, CONTAINED_DCLARE_ISSUES, DCLARE_ISSUES, CONTAINING_ATTRIBUTE, CONTAINED, READ_OBSERVEDS);

    public static DClareMPS dClareMPS() {
        return DClareMPS.instance();
    }

    public static DClareMPS dClareMPS(Transaction tx) {
        return DClareMPS.instance(tx);
    }

    public boolean isOwned() {
        return dParent() != null;
    }

    public java.util.List<DAttribute> getAttributes() {
        return dClass().getAttributes().collect(Collectors.toList());
    }

    public java.util.Set<? extends IssueKindReportItem> getIssues() {
        return MPS_ISSUES.get(this).map(Pair::b).collect(Collectors.toSet());
    }

    @Override
    public java.util.List<DAttribute> getNonSyntheticAttributes() {
        return dClass().getNonSyntheticAttributes().collect(Collectors.toList());
    }

    @Override
    public DObjectType<?> dClass() {
        DObjectType<?> type = TYPE.get(this);
        return type == TYPE.getDefault(this) ? getBootstrapType() : type;
    }

    protected abstract DObjectType<?> getBootstrapType();

    @SuppressWarnings("unchecked")
    public Collection<DMutable> getAllChildren() {
        return (Collection<DMutable>) dChildren();
    }

    @SuppressWarnings("unchecked")
    public static Set<DMutable> getDObjectSet(Object v) {
        if (v instanceof Collection) {
            return ((Collection) v).asSet();
        } else if (v instanceof java.util.Collection) {
            return Set.of((java.util.Collection) v);
        } else {
            return v == null ? Set.of() : Set.of((DMutable) v);
        }
    }

    @Override
    public void dActivate(LeafTransaction tx) {
        Mutable.super.dActivate(tx);
        start(dClareMPS());
    }

    @Override
    public void dDeactivate(LeafTransaction tx) {
        Mutable.super.dDeactivate(tx);
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

    public DMutable dObjectParent() {
        Object parent = dParent();
        return parent instanceof DMutable ? (DMutable) parent : null;
    }

    protected abstract DObjectType<?> getType();

    public static <O extends DMutable> NonCheckingObserver<O> observer(String id, Consumer<O> action, LeafModifier<?>... modifiers) {
        return NonCheckingObserver.of(id, action, modifiers);
    }

    public static <O extends DMutable, V> NonCheckingObserver<O> observer(Setable<O, V> setable, Function<O, V> value, LeafModifier<?>... modifiers) {
        return NonCheckingObserver.of(setable, value, modifiers);
    }

    public boolean isDclareOnly() {
        return CONTAINING_ATTRIBUTE.get(this) != null;
    }

    @Override
    public boolean dCheckConsistency() {
        return !isExternal();
    }

    @Override
    public ConstantState dMemoization(AbstractDerivationTransaction tx) {
        ConstantState constantState = tx.universeTransaction().constantState();
        return constantState.isSet(tx, this, Mutable.D_PARENT_CONTAINING.constant()) || isExternal() ? constantState : Mutable.super.dMemoization(tx);
    }

    @Override
    public boolean dIsOrphan(State state) {
        return Mutable.super.dIsOrphan(state) && !isExternal() && !isConstantContained();
    }

    private boolean isConstantContained() {
        LeafTransaction tx = LeafTransaction.getCurrent();
        return tx.universeTransaction().constantState().isSet(tx, this, Mutable.D_PARENT_CONTAINING.constant());
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

    protected boolean isAction() {
        return LeafTransaction.getCurrent() instanceof ActionTransaction;
    }

    protected abstract boolean isRead();

    @SuppressWarnings("unchecked")
    @Override
    public Pair<Mutable, Setable<Mutable, ?>> dParentContaining() {
        if (isRead() && readConstant()) {
            return (Pair) readParent();
        }
        activate(false);
        return Mutable.super.dParentContaining();
    }

    @Override
    public void dChangedParentContaining(Pair<Mutable, Setable<Mutable, ?>> pre, Pair<Mutable, Setable<Mutable, ?>> post) {
        if (post != null && post.a() instanceof DMutable) {
            ((DMutable) post.a()).activate(true);
        }
        Mutable.super.dChangedParentContaining(pre, post);
    }

    protected void activate(boolean changed) {
    }

    protected void readChildren() {
    }

    protected abstract Pair<DMutable, DObserved<DMutable, ?>> readParent();

    @SuppressWarnings("unchecked")
    @Override
    public Collection<? extends Observer<?>> dAllObservers() {
        return !isExternal() ? Mutable.super.dAllObservers() : (Collection) dClass().observers();
    }

    protected boolean isActive() {
        return !isExternal() && LeafTransaction.getCurrent().current().getRaw(this, Mutable.D_PARENT_CONTAINING) != null;
    }

    protected boolean isNative(INativeGroup ng) {
        return !isExternal() && !dClass().getNatives(ng).isEmpty();
    }

    protected void doActivate() {
    }
}
