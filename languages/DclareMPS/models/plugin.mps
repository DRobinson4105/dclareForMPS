<?xml version="1.0" encoding="UTF-8"?>
<model ref="r:76834813-368a-40cb-b154-43230d04bc7e(DclareMPS.plugin)">
  <persistence version="9" />
  <languages>
    <use id="f159adf4-3c93-40f9-9c5a-1f245a8697af" name="jetbrains.mps.lang.aspect" version="2" />
    <use id="696c1165-4a59-463b-bc5d-902caab85dd0" name="jetbrains.mps.make.facet" version="0" />
    <use id="28f9e497-3b42-4291-aeba-0a1039153ab1" name="jetbrains.mps.lang.plugin" version="5" />
    <use id="c9d137c4-3259-44f8-80ff-33ab2b506ee4" name="jetbrains.mps.lang.util.order" version="0" />
    <use id="982eb8df-2c96-4bd7-9963-11712ea622e5" name="jetbrains.mps.lang.resources" version="2" />
    <devkit ref="fbc25dd2-5da4-483a-8b19-70928e1b62d7(jetbrains.mps.devkit.general-purpose)" />
  </languages>
  <imports>
    <import index="f7uj" ref="r:8ffb9fde-829b-4ee3-ade6-f4eee43e66a8(jetbrains.mps.lang.typesystem.plugin)" />
  </imports>
  <registry>
    <language id="982eb8df-2c96-4bd7-9963-11712ea622e5" name="jetbrains.mps.lang.resources">
      <concept id="8974276187400029883" name="jetbrains.mps.lang.resources.structure.FileIcon" flags="ng" index="1QGGSu">
        <property id="2756621024541341363" name="file" index="1iqoE4" />
      </concept>
    </language>
    <language id="f159adf4-3c93-40f9-9c5a-1f245a8697af" name="jetbrains.mps.lang.aspect">
      <concept id="3433054418424672374" name="jetbrains.mps.lang.aspect.structure.SimpleLanguageAspectDescriptor" flags="ng" index="3vrhyV">
        <child id="6106419185511570295" name="mainLanguages" index="QG$2i" />
        <child id="8222125370833980146" name="devkit" index="TkT64" />
        <child id="3433054418425083029" name="icon" index="3vqPLo" />
        <child id="2343319097654255233" name="order" index="3F_NFc" />
      </concept>
      <concept id="174635545557784815" name="jetbrains.mps.lang.aspect.structure.SimpleAspectOrderRef" flags="ng" index="1SjbrP" />
    </language>
    <language id="c9d137c4-3259-44f8-80ff-33ab2b506ee4" name="jetbrains.mps.lang.util.order">
      <concept id="2450897840534683979" name="jetbrains.mps.lang.util.order.structure.OrderParticipantReference" flags="nn" index="2vPdvi">
        <reference id="2450897840534683980" name="target" index="2vPdvl" />
      </concept>
      <concept id="2450897840534683975" name="jetbrains.mps.lang.util.order.structure.OrderDeclaration" flags="ng" index="2vPdvu">
        <child id="2450897840534683977" name="seq" index="2vPdvg" />
      </concept>
    </language>
    <language id="7866978e-a0f0-4cc7-81bc-4d213d9375e1" name="jetbrains.mps.lang.smodel">
      <concept id="8222125370833354011" name="jetbrains.mps.lang.smodel.structure.DevkitPointer" flags="ng" index="T9mpH">
        <property id="8222125370833354012" name="devkitName" index="T9mpE" />
        <property id="8222125370833354013" name="devkitId" index="T9mpF" />
      </concept>
      <concept id="3542851458883438784" name="jetbrains.mps.lang.smodel.structure.LanguageId" flags="nn" index="2V$Bhx">
        <property id="3542851458883439831" name="namespace" index="2V$B1Q" />
        <property id="3542851458883439832" name="languageId" index="2V$B1T" />
      </concept>
    </language>
    <language id="ceab5195-25ea-4f22-9b92-103b95ca8c0c" name="jetbrains.mps.lang.core">
      <concept id="1169194658468" name="jetbrains.mps.lang.core.structure.INamedConcept" flags="ng" index="TrEIO">
        <property id="1169194664001" name="name" index="TrG5h" />
      </concept>
    </language>
  </registry>
  <node concept="3vrhyV" id="59OKK4YckpT">
    <property role="TrG5h" value="rules" />
    <node concept="2V$Bhx" id="3_UgbASWVPj" role="QG$2i">
      <property role="2V$B1T" value="c32b788d-8e4b-4023-97f5-3e90d04ed77b" />
      <property role="2V$B1Q" value="DclareMPS" />
    </node>
    <node concept="1QGGSu" id="59OKK4YdgrE" role="3vqPLo">
      <property role="1iqoE4" value="${module}/icons/Dclare.png" />
    </node>
    <node concept="2vPdvu" id="59OKK4YqMVw" role="3F_NFc">
      <node concept="1SjbrP" id="59OKK4YqMVF" role="2vPdvg">
        <ref role="2vPdvl" to="f7uj:2LiUEk8oQ$g" resolve="typesystem" />
      </node>
      <node concept="1SjbrP" id="59OKK4YqMVB" role="2vPdvg">
        <ref role="2vPdvl" node="59OKK4YckpT" resolve="rules" />
      </node>
    </node>
    <node concept="T9mpH" id="4wbMdoKTTi1" role="TkT64">
      <property role="T9mpF" value="fbc25dd2-5da4-483a-8b19-70928e1b62d7" />
      <property role="T9mpE" value="jetbrains.mps.devkit.general-purpose" />
    </node>
  </node>
</model>

