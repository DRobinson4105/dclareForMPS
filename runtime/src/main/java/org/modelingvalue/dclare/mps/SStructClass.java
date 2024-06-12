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

import org.jetbrains.mps.openapi.language.SLanguage;
import org.modelingvalue.collections.Collection;
import org.modelingvalue.collections.List;
import org.modelingvalue.collections.Set;

@SuppressWarnings("unused")
public class SStructClass {

    @SuppressWarnings("rawtypes")
    public static SStructClass of(String id, String name, SLanguage language, DAttribute[] identity, SStructClass[] supers, boolean isValueClass) {
        return new SStructClass(id, name, language, List.of(identity), Set.of(supers), isValueClass);
    }

    public static <C, V> SStructClass of(SLanguage language, String id) {
        return DClareMPS.STRUCT_CLASS_MAP.get(language).get(id);
    }

    private final String                 id;
    private final String                 name;
    private final List<DAttribute<?, ?>> identity;
    private final Set<SStructClass>      supers;
    private final SLanguage              language;
    private final boolean                isValueClass;
    private final List<DAttribute<?, ?>> completeIdentity;

    private SStructClass(String id, String name, SLanguage language, List<DAttribute<?, ?>> identity, Set<SStructClass> supers, boolean isValueClass) {
        super();
        this.id = id;
        this.name = name;
        this.language = language;
        this.supers = supers;
        this.identity = identity;
        this.isValueClass = isValueClass;
        this.completeIdentity = Collection.concat(supers.flatMap(SStructClass::getCompleteIdentity), identity).distinct().asList();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public String id() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        } else {
            SStructClass other = (SStructClass) obj;
            return id.equals(other.id);
        }
    }

    public boolean isAssignableFrom(SStructClass sub) {
        return equals(sub) || sub.supers.anyMatch(this::isAssignableFrom);
    }

    @Override
    public String toString() {
        return name;
    }

    public SLanguage getLanguage() {
        return language;
    }

    public List<DAttribute<?, ?>> getIdentity() {
        return identity;
    }

    public List<DAttribute<?, ?>> getCompleteIdentity() {
        return completeIdentity;
    }

    public Set<SStructClass> getSupers() {
        return supers;
    }

    public boolean isValueClass() {
        return isValueClass;
    }

}
