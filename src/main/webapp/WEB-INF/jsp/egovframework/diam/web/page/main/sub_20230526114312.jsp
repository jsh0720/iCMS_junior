<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 오시는길 -->

<!-- * 카카오맵 - 지도퍼가기 -->
<!-- 1. 지도 노드 -->
<div id="daumRoughmapContainer1685069153680" class="root_daum_roughmap root_daum_roughmap_landing" style="width: 100%;"></div>
<!--
	2. 설치 스크립트
	* 지도 퍼가기 서비스를 2개 이상 넣을 경우, 설치 스크립트는 하나만 삽입합니다.
-->
<script charset="UTF-8" class="daum_roughmap_loader_script" src="https://ssl.daumcdn.net/dmaps/map_js_init/roughmapLoader.js"></script>

<!-- 3. 실행 스크립트 -->
<script charset="UTF-8">
new daum.roughmap.Lander({
	"timestamp" : "1685069153680",
	"key" : "2ex5s",
	//"mapWidth" : "640",
	"mapHeight" : "460"
}).render();
</script>


<div class="dm-loc">
	<div class="dm-loc-info">
		<h5>Address</h5>
		<dl class="dm-loc-addr">
			<dt>본사·스튜디오.</dt>
			<dd>광주광역시 광산구 단전둘레길 18, 3층 (하남동)</dd>
		</dl>
		<dl class="dm-loc-addr">
			<dt>전남지사.</dt>
			<dd>전남 나주시 배멧2길 39, 315호 (빛가람동)</dd>
		</dl>
	</div>
	<div class="dm-loc-info ff-ms">
		<h5>Request a project</h5>
		<dl class="dm-loc-tel">
			<dt>Tel.</dt>
			<dd>1833.6912</dd>
		</dl>
		<dl class="dm-loc-fax">
			<dt>Fax.</dt>
			<dd>062.945.0914</dd>
		</dl>
		<dl class="dm-loc-mail">
			<dt>Email.</dt>
			<dd>admin@diam.kr</dd>
		</dl>
	</div>
	<!-- //.dm-loc-info -->
</div>
<!-- //.dm-loc -->