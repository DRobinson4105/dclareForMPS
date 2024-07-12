<?xml version="1.0" encoding="UTF-8"?>
<model ref="r:0049729d-ba70-461b-8d8a-b0755e3bfabb(DclareGui.structure)">
  <persistence version="9" />
  <languages>
    <devkit ref="78434eb8-b0e5-444b-850d-e7c4ad2da9ab(jetbrains.mps.devkit.aspect.structure)" />
  </languages>
  <imports>
    <import index="tpce" ref="r:00000000-0000-4000-0000-011c89590292(jetbrains.mps.lang.structure.structure)" />
    <import index="7ggn" ref="r:38c3b7db-3776-47fb-ba27-a4f002c4edc7(DclareMPS.structure)" />
    <import index="tpck" ref="r:00000000-0000-4000-0000-011c89590288(jetbrains.mps.lang.core.structure)" implicit="true" />
    <import index="hypd" ref="r:aa31e43e-9240-4f4d-b6db-5c1c9a86c59e(jetbrains.mps.lang.project.structure)" implicit="true" />
  </imports>
  <registry>
    <language id="982eb8df-2c96-4bd7-9963-11712ea622e5" name="jetbrains.mps.lang.resources">
      <concept id="2756621024541681841" name="jetbrains.mps.lang.resources.structure.Primitive" flags="ng" index="1irPi6">
        <child id="1860120738943552529" name="fillColor" index="3PKjn_" />
      </concept>
      <concept id="2756621024541681849" name="jetbrains.mps.lang.resources.structure.Text" flags="ng" index="1irPie">
        <property id="2756621024541681854" name="text" index="1irPi9" />
        <child id="1860120738943552534" name="color" index="3PKjny" />
      </concept>
      <concept id="2756621024541674821" name="jetbrains.mps.lang.resources.structure.TextIcon" flags="ng" index="1irR5M">
        <property id="1358878980655415353" name="iconId" index="2$rrk2" />
        <child id="2756621024541675110" name="layers" index="1irR9h" />
      </concept>
      <concept id="2756621024541675105" name="jetbrains.mps.lang.resources.structure.Rect" flags="ng" index="1irR9m" />
      <concept id="1860120738943552477" name="jetbrains.mps.lang.resources.structure.ColorLiteral" flags="ng" index="3PKj8D">
        <property id="1860120738943552481" name="val" index="3PKj8l" />
      </concept>
    </language>
    <language id="c72da2b9-7cce-4447-8389-f407dc1158b7" name="jetbrains.mps.lang.structure">
      <concept id="1169125787135" name="jetbrains.mps.lang.structure.structure.AbstractConceptDeclaration" flags="ig" index="PkWjJ">
        <property id="6714410169261853888" name="conceptId" index="EcuMT" />
        <child id="1071489727083" name="linkDeclaration" index="1TKVEi" />
        <child id="1071489727084" name="propertyDeclaration" index="1TKVEl" />
      </concept>
      <concept id="1169125989551" name="jetbrains.mps.lang.structure.structure.InterfaceConceptDeclaration" flags="ig" index="PlHQZ">
        <child id="1169127546356" name="extends" index="PrDN$" />
      </concept>
      <concept id="1169127622168" name="jetbrains.mps.lang.structure.structure.InterfaceConceptReference" flags="ig" index="PrWs8">
        <reference id="1169127628841" name="intfc" index="PrY4T" />
      </concept>
      <concept id="1071489090640" name="jetbrains.mps.lang.structure.structure.ConceptDeclaration" flags="ig" index="1TIwiD">
        <property id="1096454100552" name="rootable" index="19KtqR" />
        <reference id="1071489389519" name="extends" index="1TJDcQ" />
        <child id="6327362524875300597" name="icon" index="rwd14" />
        <child id="1169129564478" name="implements" index="PzmwI" />
      </concept>
      <concept id="1071489288299" name="jetbrains.mps.lang.structure.structure.PropertyDeclaration" flags="ig" index="1TJgyi">
        <property id="241647608299431129" name="propertyId" index="IQ2nx" />
        <reference id="1082985295845" name="dataType" index="AX2Wp" />
      </concept>
      <concept id="1071489288298" name="jetbrains.mps.lang.structure.structure.LinkDeclaration" flags="ig" index="1TJgyj">
        <property id="1071599776563" name="role" index="20kJfa" />
        <property id="1071599893252" name="sourceCardinality" index="20lbJX" />
        <property id="1071599937831" name="metaClass" index="20lmBu" />
        <property id="241647608299431140" name="linkId" index="IQ2ns" />
        <reference id="1071599976176" name="target" index="20lvS9" />
      </concept>
    </language>
    <language id="ceab5195-25ea-4f22-9b92-103b95ca8c0c" name="jetbrains.mps.lang.core">
      <concept id="1133920641626" name="jetbrains.mps.lang.core.structure.BaseConcept" flags="ng" index="2VYdi">
        <property id="1193676396447" name="virtualPackage" index="3GE5qa" />
      </concept>
      <concept id="1169194658468" name="jetbrains.mps.lang.core.structure.INamedConcept" flags="ngI" index="TrEIO">
        <property id="1169194664001" name="name" index="TrG5h" />
      </concept>
    </language>
  </registry>
  <node concept="1TIwiD" id="31sbKqyPLDe">
    <property role="EcuMT" value="3484711917226236494" />
    <property role="TrG5h" value="NodeLayout" />
    <property role="3GE5qa" value="diagrams" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyi" id="31sbKqyPLDJ" role="1TKVEl">
      <property role="IQ2nx" value="3484711917226236527" />
      <property role="TrG5h" value="x" />
      <ref role="AX2Wp" to="tpck:fKAQMTA" resolve="integer" />
    </node>
    <node concept="1TJgyi" id="31sbKqyPLDL" role="1TKVEl">
      <property role="IQ2nx" value="3484711917226236529" />
      <property role="TrG5h" value="y" />
      <ref role="AX2Wp" to="tpck:fKAQMTA" resolve="integer" />
    </node>
    <node concept="1TJgyj" id="31sbKqyPLDf" role="1TKVEi">
      <property role="IQ2ns" value="3484711917226236495" />
      <property role="20kJfa" value="node" />
      <property role="20lbJX" value="fLJekj4/_1" />
      <ref role="20lvS9" to="tpck:gw2VY9q" resolve="BaseConcept" />
    </node>
    <node concept="1TJgyj" id="31sbKqyPLEi" role="1TKVEi">
      <property role="IQ2ns" value="3484711917226236562" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="edges" />
      <property role="20lbJX" value="fLJekj5/_0__n" />
      <ref role="20lvS9" node="31sbKqyPLDj" resolve="EdgeLayout" />
    </node>
  </node>
  <node concept="1TIwiD" id="31sbKqyPLDj">
    <property role="EcuMT" value="3484711917226236499" />
    <property role="TrG5h" value="EdgeLayout" />
    <property role="3GE5qa" value="diagrams" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyj" id="31sbKqyPMf4" role="1TKVEi">
      <property role="IQ2ns" value="3484711917226238916" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="joints" />
      <property role="20lbJX" value="fLJekj5/_0__n" />
      <ref role="20lvS9" node="31sbKqyPLDm" resolve="JointLayout" />
    </node>
    <node concept="1TJgyj" id="31sbKqyPLDk" role="1TKVEi">
      <property role="IQ2ns" value="3484711917226236500" />
      <property role="20kJfa" value="association" />
      <property role="20lbJX" value="fLJekj4/_1" />
      <ref role="20lvS9" to="tpck:gw2VY9q" resolve="BaseConcept" />
    </node>
  </node>
  <node concept="1TIwiD" id="31sbKqyPLDm">
    <property role="EcuMT" value="3484711917226236502" />
    <property role="TrG5h" value="JointLayout" />
    <property role="3GE5qa" value="diagrams" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyi" id="3VTkizYHXZo" role="1TKVEl">
      <property role="IQ2nx" value="4537747336066490328" />
      <property role="TrG5h" value="identity" />
      <ref role="AX2Wp" to="tpck:fKAQMTA" resolve="integer" />
    </node>
    <node concept="1TJgyi" id="31sbKqyPMf7" role="1TKVEl">
      <property role="IQ2nx" value="3484711917226238919" />
      <property role="TrG5h" value="x" />
      <ref role="AX2Wp" to="tpck:fKAQMTA" resolve="integer" />
    </node>
    <node concept="1TJgyi" id="31sbKqyPMf9" role="1TKVEl">
      <property role="IQ2nx" value="3484711917226238921" />
      <property role="TrG5h" value="y" />
      <ref role="AX2Wp" to="tpck:fKAQMTA" resolve="integer" />
    </node>
  </node>
  <node concept="PlHQZ" id="3psr82$c6ii">
    <property role="TrG5h" value="ISingleRootComponent" />
    <property role="EcuMT" value="699348237883893817" />
    <property role="3GE5qa" value="gui" />
    <node concept="PrWs8" id="7jkrzkVxKDb" role="PrDN$">
      <ref role="PrY4T" node="7jkrzkVxKBQ" resolve="IRootComponentContainer" />
    </node>
  </node>
  <node concept="PlHQZ" id="3psr82$eZ54">
    <property role="TrG5h" value="IDiagram" />
    <property role="3GE5qa" value="diagrams" />
    <property role="EcuMT" value="3484711917226236493" />
    <node concept="1TJgyi" id="2Q8IysVI4Ud" role="1TKVEl">
      <property role="IQ2nx" value="3281077006207831693" />
      <property role="TrG5h" value="autoLayout" />
      <ref role="AX2Wp" to="tpck:fKAQMTB" resolve="boolean" />
    </node>
    <node concept="1TJgyi" id="5Y0My$awdrh" role="1TKVEl">
      <property role="IQ2nx" value="6881722508240803537" />
      <property role="TrG5h" value="antiCrossings" />
      <ref role="AX2Wp" to="tpck:fKAQMTB" resolve="boolean" />
    </node>
    <node concept="1TJgyi" id="3SChBII2aLU" role="1TKVEl">
      <property role="IQ2nx" value="4478907326434684026" />
      <property role="TrG5h" value="autoCentre" />
      <ref role="AX2Wp" to="tpck:fKAQMTB" resolve="boolean" />
    </node>
    <node concept="1TJgyj" id="31sbKqyPLDh" role="1TKVEi">
      <property role="IQ2ns" value="3484711917226236497" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="nodes" />
      <property role="20lbJX" value="fLJekj5/_0__n" />
      <ref role="20lvS9" node="31sbKqyPLDe" resolve="NodeLayout" />
    </node>
    <node concept="PrWs8" id="3psr82$cO_x" role="PrDN$">
      <ref role="PrY4T" node="3psr82$c6ii" resolve="ISingleRootComponent" />
    </node>
  </node>
  <node concept="1TIwiD" id="67eN9QkffP5">
    <property role="EcuMT" value="7047795444050361669" />
    <property role="TrG5h" value="ConceptDiagram" />
    <property role="3GE5qa" value="concept_diagram" />
    <property role="19KtqR" value="true" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyi" id="12WXgwl7Bd8" role="1TKVEl">
      <property role="IQ2nx" value="1206108215291310920" />
      <property role="TrG5h" value="manual" />
      <ref role="AX2Wp" to="tpck:fKAQMTB" resolve="boolean" />
    </node>
    <node concept="1TJgyi" id="7jkrzkZwtHY" role="1TKVEl">
      <property role="IQ2nx" value="8418474778440096638" />
      <property role="TrG5h" value="virtualPackageFilter" />
      <ref role="AX2Wp" to="tpck:fKAOsGN" resolve="string" />
    </node>
    <node concept="PrWs8" id="67eN9QkffP6" role="PzmwI">
      <ref role="PrY4T" node="3psr82$eZ54" resolve="IDiagram" />
    </node>
    <node concept="PrWs8" id="67eN9QkffP9" role="PzmwI">
      <ref role="PrY4T" to="tpck:h0TrEE$" resolve="INamedConcept" />
    </node>
    <node concept="1TJgyj" id="67eN9Qkfp8P" role="1TKVEi">
      <property role="IQ2ns" value="7047795444050399797" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="concepts" />
      <property role="20lbJX" value="fLJekj5/_0__n" />
      <ref role="20lvS9" node="67eN9Qkg3uD" resolve="ConceptRef" />
    </node>
    <node concept="1TJgyj" id="7aFw1spS2SV" role="1TKVEi">
      <property role="IQ2ns" value="8262838778048032315" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="drawnModuleREf" />
      <property role="20lbJX" value="fLJekj4/_1" />
      <ref role="20lvS9" to="hypd:1AZH1sZHi4c" resolve="ModuleReference" />
    </node>
    <node concept="1irR5M" id="67eN9QkChOV" role="rwd14">
      <property role="2$rrk2" value="101" />
      <node concept="1irR9m" id="67eN9QkChP6" role="1irR9h">
        <node concept="3PKj8D" id="67eN9QkChPd" role="3PKjn_">
          <property role="3PKj8l" value="00ddff" />
        </node>
      </node>
      <node concept="1irPie" id="67eN9QkChPl" role="1irR9h">
        <property role="1irPi9" value="D" />
        <node concept="3PKj8D" id="67eN9QkChPs" role="3PKjny">
          <property role="3PKj8l" value="000000" />
        </node>
      </node>
    </node>
  </node>
  <node concept="1TIwiD" id="67eN9Qkg3uD">
    <property role="EcuMT" value="7047795444050573225" />
    <property role="3GE5qa" value="concept_diagram" />
    <property role="TrG5h" value="ConceptRef" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyj" id="67eN9Qkg3uE" role="1TKVEi">
      <property role="IQ2ns" value="7047795444050573226" />
      <property role="20kJfa" value="concept" />
      <property role="20lbJX" value="fLJekj4/_1" />
      <ref role="20lvS9" to="tpce:h0PkWnZ" resolve="AbstractConceptDeclaration" />
    </node>
  </node>
  <node concept="1TIwiD" id="5hEUvARta2y">
    <property role="EcuMT" value="6082931505451278498" />
    <property role="TrG5h" value="StructDiagram" />
    <property role="3GE5qa" value="struct_diagram" />
    <property role="19KtqR" value="true" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyi" id="2$yK5YK5zMw" role="1TKVEl">
      <property role="IQ2nx" value="2964143021971881120" />
      <property role="TrG5h" value="manual" />
      <ref role="AX2Wp" to="tpck:fKAQMTB" resolve="boolean" />
    </node>
    <node concept="1TJgyj" id="5hEUvARtaAj" role="1TKVEi">
      <property role="IQ2ns" value="6082931505451280787" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="structs" />
      <property role="20lbJX" value="fLJekj5/_0__n" />
      <ref role="20lvS9" node="5hEUvARtaxp" resolve="StructRef" />
    </node>
    <node concept="1TJgyj" id="2$yK5YK4u0F" role="1TKVEi">
      <property role="IQ2ns" value="2964143021971595307" />
      <property role="20lmBu" value="fLJjDmT/aggregation" />
      <property role="20kJfa" value="drawnModuleREf" />
      <property role="20lbJX" value="fLJekj4/_1" />
      <ref role="20lvS9" to="hypd:1AZH1sZHi4c" resolve="ModuleReference" />
    </node>
    <node concept="PrWs8" id="5hEUvARta2z" role="PzmwI">
      <ref role="PrY4T" node="3psr82$eZ54" resolve="IDiagram" />
    </node>
    <node concept="PrWs8" id="5hEUvARta2_" role="PzmwI">
      <ref role="PrY4T" to="tpck:h0TrEE$" resolve="INamedConcept" />
    </node>
    <node concept="1irR5M" id="5hEUvARta2C" role="rwd14">
      <property role="2$rrk2" value="104" />
      <node concept="1irR9m" id="5hEUvARta2G" role="1irR9h">
        <node concept="3PKj8D" id="5hEUvARta2Z" role="3PKjn_">
          <property role="3PKj8l" value="00ddff" />
        </node>
      </node>
      <node concept="1irPie" id="5hEUvARta2P" role="1irR9h">
        <property role="1irPi9" value="D" />
        <node concept="3PKj8D" id="5hEUvARta2W" role="3PKjny">
          <property role="3PKj8l" value="000055" />
        </node>
      </node>
    </node>
  </node>
  <node concept="1TIwiD" id="5hEUvARtaxp">
    <property role="EcuMT" value="6082931505451280473" />
    <property role="3GE5qa" value="struct_diagram" />
    <property role="TrG5h" value="StructRef" />
    <ref role="1TJDcQ" to="tpck:gw2VY9q" resolve="BaseConcept" />
    <node concept="1TJgyj" id="5hEUvARtaxq" role="1TKVEi">
      <property role="IQ2ns" value="6082931505451280474" />
      <property role="20kJfa" value="struct" />
      <property role="20lbJX" value="fLJekj4/_1" />
      <ref role="20lvS9" to="7ggn:4$bpWrNHO7E" resolve="StructClass" />
    </node>
  </node>
  <node concept="PlHQZ" id="7jkrzkVxKBQ">
    <property role="EcuMT" value="8418474778373327350" />
    <property role="3GE5qa" value="gui" />
    <property role="TrG5h" value="IRootComponentContainer" />
  </node>
</model>

