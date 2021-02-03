//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
// (C) Copyright 2018-2021 Modeling Value Group B.V. (http://modelingvalue.org)                                        ~
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

defaultTasks(
    "mvgcorrector",
    "test",
    "mpsant-clean",
    "mpsant-cleanSources",
    "mpsant-generate",
    "mpsant-build",
    "mvgtagger",
    "mvguploader"
)

plugins {
    id("org.modelingvalue.gradle.mvgplugin") version "0.4.22"
}

// import ant file:
ant.setProperty("mps_home", mvgmps.mpsInstallDir.toString())
ant.setProperty("version", version)
ant.setProperty("versionExtra", mvgmps.versionExtra)
ant.setProperty("versionStamp", mvgmps.versionStamp)
ant.importBuild("mps_build.xml") {
    "mpsant-$it"
}
tasks.filter {
    it.name.startsWith("mpsant-")
}.forEach {
    // the runtime jars should be build and gathered first:
    it.dependsOn(":runtime:gatherRuntimeJars")
    // always set the properties first:
    it.doFirst {
        ant.setProperty("mps_home", mvgmps.mpsInstallDir.toString())
        ant.setProperty("version", version)
        ant.setProperty("versionExtra", mvgmps.versionExtra)
        ant.setProperty("versionStamp", mvgmps.versionStamp)
    }
}

// upload plugin to jetbrains
mvguploader {
    pluginId = "13797"
}