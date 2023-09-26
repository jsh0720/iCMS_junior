<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% 
	String dm_id = request.getParameter("dm_id") != null ? request.getParameter("dm_id").trim() : ""; 
	String type = "insert";
	if (dm_id != null && !"".equals(dm_id)) {
		type = "update";
	} else{
		type = "insert";
	}
%>
<c:set var="dm_id" value="<%=dm_id%>"/>
<c:set var="type" value="<%=type%>"/>
<c:import url="/adm/page_header.do" />
<div class="easyui-layout">
	<div id="form_wrap">
		<form id="fm" name="fm" action="" method="post">
	       	<input type="hidden" name="type" id="type" value="<c:out value='${type}'/>" /> 
	   	 	<c:if test='${type eq "update"}'>
	 			<input type="hidden" name="dm_id" id="dm_id" value="<c:out value='${dm_id}'/>" />
	        </c:if>
	        <div class="title">
	        	<h1>게시판관리</h1>
		        <div class="btnWrap">
			        <button type="button" id="fnSave" onclick="createNewBoard()">저장</button>
			        <button type="button" id="fnCancel" class="bt09" onclick="cancleTheCurrentOperation()">취소</button>
		        </div>
	        </div>
	       	<div class="page">
	        	<h3>게시판정보</h3>
	        	<p class="required_text"><span class="required_value">*</span>표시된 입력값은 필수입력값입니다.</p>
				<dl>
					<c:choose>
						<c:when test="${type eq 'insert'}">
							<dt><label for="dm_domain">도메인선택<span class="required_value">*</span></label></dt>
						    <dd>
						        <select id="dm_domain" name="dm_domain" class="easyui-combobox" panelHeight="auto"
						          data-options="url: '/adm/select_domain_id.do',
										method: 'get',
										valueField: 'dm_id',
				                        textField: 'dm_domain_nm',
										editable: false">
						 		</select>
						    </dd>
						</c:when>
						<c:otherwise>
							<dt>도메인선택</dt>
							<dd>
								<span id="dm_domain_text"></span>
								<input type="hidden" name="dm_domain" id="dm_domain"/>
							</dd>
						</c:otherwise>
					</c:choose>				    
				</dl>
				<dl>
				    <dt><label for="bo_table">게시판 아이디<c:if test='${type eq "insert"}'><span class="required_value">*</span></c:if></label></dt>
				    <dd>
				        <input id="dm_table" name="dm_table" type="text" maxlength="20" autocomplete="off" onkeyup="setEngPattern(this);">
				        <c:if test='${type eq "insert"}'>
				        	<p class="noty">게시판 ID를 입력해주세요. 영문과 숫자만 입력가능하며 최대 20자까지 입력가능합니다.</p>
				        </c:if>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_subject">게시판명<span class="required_value">*</span></label></dt>
				    <dd>
				        <input id="dm_subject" name="dm_subject" type="text" maxlength="100" autocomplete="off"/>
				        <p class="noty">게시판 명을 입력해주세요. 최대 100자까지 입력가능합니다.</p>
				    </dd>
				</dl>
				<dl>
					<dt><label for="gr_id">스킨<span class="required_value">*</span></label></dt>
					<dd>
						<select id="dm_skin" name="dm_skin" class="easyui-combobox" panelHeight="auto"
						                   data-options="url: '/adm/select_board_skin.do',
											method: 'get',
											valueField: 'dm_code_value',
											textField: 'dm_code_name',
											editable: false">
						</select>
					</dd>
				</dl>
				<dl>
					<dt><label for="dm_use_category">분류사용</label></dt>
					<dd>
			    		<label><input type="checkbox" name="dm_use_category" id="dm_use_category" value="1">사용</label>
				    	<input type="text" name="dm_category_list" id="dm_category_list" value="" class="wd90" onkeyup="categoryCheck(this)" onkeydown="characterCheck(this)" placeholder="예시) 카테고리1,카테고리2,카테고리3" maxlength="255" autocomplete="off" style="display:none;"/>
						<p class="noty" style="display:none;">입력하고자 하는 카테고리를 ,(콤마) 로 연결하여 입력해주세요.</p>
						<p class="noty" style="display:none;">?, !, %, /, :, ;, ., #, ?, @, &gt;, &lt;, %, &amp;, =, $, +, ~, \ 에 해당하는 특수문자는 입력할 수 없습니다.</p>
					</dd>
				</dl>
				<dl>
				   	<dt><label for="dm_use_list_category">분류탭 노출</label></dt>
				    <dd>
				    	<label><input type="checkbox" name="dm_use_list_category" id="dm_use_list_category" value="1">사용</label>
				    </dd>
				</dl>
				<h3>메인페이지 노출 설정</h3>
				<dl>
				    <dt><label for="bo_table">메인페이지 사용<span class="required_value">*</span></label></dt>
				    <dd>
				        <label><input type="radio" id="dm_main_use" name="dm_main_use" value="0" checked>미사용</label>
				        <label><input type="radio" id="dm_main_use" name="dm_main_use" value="1">사용</label>
				        <p class="noty">미사용의 경우 메인페이지에 노출되지 않습니다.</p>
				    </dd>
				</dl>
				<dl class="useMain">
					<dt><label for="dm_main_order">게시판 노출 순서<span class="required_value">*</span></label></dt>
					<dd>
						<input id="dm_main_order" name="dm_main_order" type="text" maxlength="2" autocomplete="off" onkeyup="setNumberPattern(this);"/>
						<p class="noty">1부터 99까지의 숫자만 입력가능합니다.</p>
					</dd>
				</dl>
				<dl class="useMain">
					<dt><label for="dm_main_count">게시글 노출 건수<span class="required_value">*</span></label></dt>
					<dd>
						<input id="dm_main_count" name="dm_main_count" type="text" maxlength="1" autocomplete="off" onkeyup="setOrderPattern(this);"/>
						<p class="noty">0부터 9까지의 한자리 숫자만 입력가능합니다.</p>
					</dd>
				</dl>
				<h3>기능 &amp; 권한 설정</h3>
				<dl>
				    <dt><label for="bo_table">권한 유형 설정<span class="required_value">*</span></label></dt>
				    <dd>
				        <label><input type="radio" id="dm_auth_type" name="dm_auth_type" value="1">그룹형</label>
				        <label><input type="radio" id="dm_auth_type" name="dm_auth_type" value="2">레벨형</label>
				        <label><input type="radio" id="dm_auth_type" name="dm_auth_type" value="3" checked="checked">모든 사용자</label>
				    </dd>
				</dl>
				<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 그룹설정은 권한유형을 (그룹형)으로 설정할때만 적용됩니다. *(레벨형)일경우에는 적용되지 않습니다.</p>
				<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 그룹설정에 체크가 모두 안되있을경우에는 비회원도 접속가능합니다.</p>
				<dl>
				    <dt><label for="bo_table">리스트권한 그룹설정</label></dt>
				    <dd id="list_group">
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">읽기권한 그룹설정</label></dt>
				    <dd id="read_group">
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">쓰기권한 그룹설정</label></dt>
				    <dd id="write_group">
				    </dd>
				</dl>
				<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 그룹설정은 권한유형을 (레벨형)으로 설정할때만 적용됩니다. *(그룹형)일경우에는 적용되지 않습니다.</p>
				<dl>
					<dt><label for="dm_list_level">리스트권한 레벨설정</label></dt>
					<dd>
				       <label><input type="radio" id="dm_list_level" name="dm_list_level" value="0" checked="checked">전체(회원+비회원)</label>
				       <label><input type="radio" id="dm_list_level" name="dm_list_level" value="6">관리자 전용</label>
				       <label><input type="radio" id="dm_list_level" name="dm_list_level" value="1">회원전용(비회원제외)</label>       
				    </dd>
				</dl>
				<dl>
					<dt><label for="dm_read_level">읽기권한 레벨설정</label></dt>
					<dd>
						<label><input type="radio" id="dm_read_level" name="dm_read_level" value="0" checked="checked">전체(회원+비회원)</label>
						<label><input type="radio" id="dm_read_level" name="dm_read_level" value="6">관리자 전용</label>
						<label><input type="radio" id="dm_read_level" name="dm_read_level" value="1">회원전용(비회원제외)</label>  
					</dd>
				</dl>
				<dl>
					<dt><label for="dm_write_level">쓰기권한 레벨설정</label></dt>
					<dd>
					    <label><input type="radio" id="dm_write_level" name="dm_write_level" value="0" checked="checked">전체(회원+비회원)</label>
					    <label><input type="radio" id="dm_write_level" name="dm_write_level" value="6">관리자 전용</label>
					    <label><input type="radio" id="dm_write_level" name="dm_write_level" value="1">회원전용(비회원제외)</label>
					</dd>
				</dl>
	       		<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 답변설정은 설정한 그룹/레벨형 권한유형에 따라 적용됩니다. 예시) 권한유형이 그룹형일 때 선택한 그룹만 적용, 레벨은 적용X</p>
				<dl>
				    <dt><label for="dm_is_reply">답변기능</label></dt>
				    <dd>
				        <label><input type="checkbox" name="dm_is_reply" id="dm_is_reply" value="1">사용</label>
				        <span class="noty">'basic' 스킨만 답변기능이 제공됩니다.</span>
				    </dd>
				</dl>
				<dl>
				    <dt>답변권한 레벨설정</dt>
				    <dd>
				        <label><input type="radio" id="dm_reply_level" name="dm_reply_level" value="0" checked="checked">전체(회원+비회원)</label>
				        <label><input type="radio" id="dm_reply_level" name="dm_reply_level" value="6">관리자 전용</label>
				        <label><input type="radio" id="dm_reply_level" name="dm_reply_level" value="1">회원전용(비회원제외)</label>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">답변권한 그룹설정</label></dt>
				    <dd id="reply_group">
				    </dd>
				</dl>
	           	<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 댓글설정은 설정한 그룹/레벨형 권한유형에 따라 적용됩니다. 예시) 권한유형이 그룹형일 때 선택한 그룹만 적용, 레벨은 적용X</p>
				<dl>
				    <dt><label for="gr_id">댓글기능</label></dt>
				    <dd>
				        <label><input type="checkbox" name="dm_is_comment" id="dm_is_comment" value="1">사용</label>
				    </dd>
				</dl>
				<dl>
				    <dt>댓글권한 레벨설정</dt>
				    <dd>
				        <label><input type="radio" id="dm_comment_level" name="dm_comment_level" value="0" checked="checked">전체(회원+비회원)</label>
				        <label><input type="radio" id="dm_comment_level" name="dm_comment_level" value="6">관리자 전용</label>
				        <label><input type="radio" id="dm_comment_level" name="dm_comment_level" value="1">회원전용(비회원제외)</label>
				    </dd>
				</dl>
		       <dl>
		           <dt><label for="bo_table">댓글권한 그룹설정</label></dt>
		           <dd id="comment_group">
		           </dd>
		       </dl>
				<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 링크설정은 설정한 그룹/레벨형 권한유형에 따라 적용됩니다. 예시) 권한유형이 그룹형일 때 선택한 그룹만 적용, 레벨은 적용X</p>
				<dl>
				    <dt>링크권한 레벨설정</dt>
				    <dd>
				        <label><input type="radio" id="dm_link_level" name="dm_link_level" value="0" checked="checked">전체(회원+비회원)</label>
				        <label><input type="radio" id="dm_link_level" name="dm_link_level" value="6">관리자 전용</label>
				        <label><input type="radio" id="dm_link_level" name="dm_link_level" value="1">회원전용(비회원제외)</label>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">링크권한 그룹설정</label></dt>
				    <dd id="link_group">
				    </dd>
				</dl>
				<p class="noty" style="border-bottom:1px solid #ddd;padding-bottom:5px;">아래 파일업로드 설정은 설정한 그룹/레벨형 권한유형에 따라 적용됩니다. 예시) 권한유형이 그룹형일 때 선택한 그룹만 적용, 레벨은 적용X</p>
				<dl>
				    <dt>파일업로드권한 레벨설정</dt>
				    <dd>
				        <label><input type="radio" id="dm_upload_level" name="dm_upload_level" value="0" checked="checked">전체(회원+비회원)</label>
				        <label><input type="radio" id="dm_upload_level" name="dm_upload_level" value="6">관리자 전용</label>
				        <label><input type="radio" id="dm_upload_level" name="dm_upload_level" value="1">회원전용(비회원제외)</label>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">파일업로드권한 그룹설정</label></dt>
				    <dd id="upload_group">
				    </dd>
				</dl>
				
				<h3>게시글 설정</h3>
				<dl>
				    <dt>작성자 표시방법<span class="required_value">*</span></dt>
				    <dd>
				        <label><input type="radio" id="dm_writer_type" name="dm_writer_type" value="name">이름</label>
				        <label><input type="radio" id="dm_writer_type" name="dm_writer_type" value="id" checked="checked">아이디</label>
				    </dd>
				</dl>
				<dl>
				    <dt>작성자 노출제한<span class="required_value">*</span></dt>
				    <dd>
				        <select name="dm_writer_secret" id="dm_writer_secret">
				            <option value="1">모두 노출</option>
				            <option value="2">1글자 비노출</option>
				            <option value="3">1글자 제외 모두 비노출</option>
				            <option value="4">끝에 2글자 비노출</option>
				        </select>
				    </dd>
				</dl>
				<dl>
				    <dt>비밀글 설정<span class="required_value">*</span></dt>
				    <dd>
				        <label><input type="radio" id="dm_use_secret" name="dm_use_secret" value="0" checked="checked">미사용</label>
				        <label><input type="radio" id="dm_use_secret" name="dm_use_secret" value="1">사용</label>
				        <span class="noty">'basic' 스킨만 비밀글 기능이 제공됩니다.</span>
				    </dd>
				</dl>
				<dl>
				    <dt>비밀댓글 설정<span class="required_value">*</span></dt>
				    <dd>
				        <label><input type="radio" id="dm_use_comment_secret" name="dm_use_comment_secret" value="0" checked="checked">미사용</label>
				        <label><input type="radio" id="dm_use_comment_secret" name="dm_use_comment_secret" value="1">사용</label>
				    </dd>
				</dl>
				<dl>
				    <dt>조회당 Hit증가수<span class="required_value">*</span></dt>
				    <dd>
				        <input type="text" class="wd10" name="dm_hit_count" id="dm_hit_count" value="1"> 개 &nbsp;
				    </dd>
				</dl>
				<dl>
					<dt>HIT 아이콘</dt>
					<dd>
						<label><input type="checkbox" name="dm_is_hit" value="1" id="dm_is_hit">사용</label>
						<label>
							<span class="verti_middle"><span class="required_value">*</span>조회수</span>
							<input type="text" style="width:50px" name="dm_hit_max" id="dm_hit_max" value="100">
							<span class="verti_middle">회 이상</span>
						</label>
						<p id="hit_ex" style="display:none;"> 등록하지 않으면 기본 아이콘으로 노출됩니다.</p>
						<p id="hit_file" style="display:none;">
							<input type="file" name="dm_hit_icon_file" id="dm_hit_icon_file" value="" class="wd30 line_height_28" />
						</p>
					</dd>
				</dl>
	      		<dl>
					<dt>NEW 아이콘</dt>
					<dd>
						<label><input type="checkbox" name="dm_is_new" value="1" id="dm_is_new">사용</label>
						<label>
							<span class="verti_middle"><span class="required_value">*</span>게시글 등록 후</span>
							<input type="text" style="width:50px" name="dm_new_time" id="dm_new_time" value="12">
							<span class="verti_middle">시간 이내</span>
						</label>
						<p id="new_ex" style="display:none;"> 등록하지 않으면 기본 아이콘으로 노출됩니다.</p>
						<p id="new_file" style="display:none;">
							<input type="file" name="dm_new_icon_file" id="dm_new_icon_file" value="" class="wd30 line_height_28" />
						</p>
					</dd>
				</dl>
				<dl>
				    <dt>첨부파일 아이콘</dt>
				    <dd>
				        <label><input type="checkbox" name="dm_use_file_icon" value="1" id="dm_use_file_icon">사용</label>
				        <span class="noty">게시물 리스트 중 첨부파일이 있는 게시글에 첨부파일 아이콘 출력</span>
				    </dd>
				</dl>
				<dl>
				    <dt>게시글 삭제 설정<span class="required_value">*</span></dt>
				    <dd>
				        <label><input type="radio" id="dm_reply_delete_type" name="dm_reply_delete_type" value="alone">답글/댓글이 있는 게시글 삭제시, 해당 글만 삭제</label><br>
				        <label class="padding_none"><input type="radio" id="dm_reply_delete_type" name="dm_reply_delete_type" value="both" checked="checked">답글/댓글이 있는 게시글 삭제시, 답글/댓글도 함께 삭제</label>
				    </dd>
				</dl>
				<h3>게시판 목록 설정</h3>
				<dl>
					<dt>페이지당 게시물 수<span class="required_value">*</span></dt>
					<dd>
						<p class="noty mb10">게시판 페이지별 게시글 노출 개수를 입력해주세요.</p>
						<input type="text" name="dm_page_rows" id="dm_page_rows" value="10" maxlength="2" onkeyup="setNumberPattern(this);">
				    	<p class="noty">1부터 99까지 숫자만 입력가능합니다.</p>
					</dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">썸네일 이미지 폭<span class="required_value">*</span></label></dt>
				    <dd>
				    	<p class="noty mb10">'gallery' 스킨에서 사용되는 리스트썸네일 이미지 폭을 입력해주세요.</p>
				        <input id="dm_gallery_width" name="dm_gallery_width" type="text" value="350" maxlength="3" onkeyup="setNumberPattern(this);"/>
				    	<p class="noty">3자리 숫자까지 입력가능합니다.</p>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">썸네일 이미지 높이<span class="required_value">*</span></label></dt>
				    <dd>
				    	<p class="noty mb10">'gallery' 스킨에서 사용되는 리스트썸네일 이미지 높이를 입력해주세요.</p>
				        <input id="dm_gallery_height" name="dm_gallery_height" type="text" value="220" maxlength="3" onkeyup="setNumberPattern(this);"/>
				        <p class="noty">3자리 숫자까지 입력가능합니다.</p>
				    </dd>
				</dl>
				<h3>글쓰기 설정</h3>
				<dl>
				    <dt><label for="gr_id">에디터</label></dt>
				    <dd>
				        <label><input type="checkbox" name="dm_use_dhtml_editor" value="1" id="dm_use_dhtml_editor" checked="checked">사용</label>
				        <span class="noty">댓글기능에는 에디터 기능이 제공되지 않습니다.</span>
				    </dd>
				</dl>
				<dl>
				    <dt>링크 사용</dt>
				    <dd>
				        <label><input type="checkbox" name="dm_use_link" value="1" id="dm_use_link">사용</label>
				        <span class="noty">2건의 링크를 설정할 수 있으며 'video' 스킨은 1건 이용가능합니다.</span>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="gr_id">첨부파일 사용</label></dt>
				    <dd>
				        <label><input type="checkbox" name="dm_use_file" value="1" id="dm_use_file">사용</label>
				        <span class="noty">댓글기능에는 첨부파일 기능이 제공되지 않습니다.</span>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="bo_table">첨부파일 갯수<span class="required_value">*</span></label></dt>
				    <dd>
				        <input id="dm_upload_count" name="dm_upload_count" type="text" value="2" class="wd10" maxlength="1" onkeyup="setNumberPattern(this);"/>
				        <span class="noty"><strong>최대 9개</strong>까지 설정 가능</span>
				        <p class="noty">1부터 9까지 한자리 숫자만 입력가능합니다.</p>
				    </dd>
				</dl>				
				<dl>
				    <dt>
				    	<label for="bo_table">본문 설정</label>
				    </dt>
				    <dd>
				    	<p class="noty mb10">사용자가 게시글 작성 시 기본으로 표출될 양식을 입력해주세요.</p>
				        <textarea id="dm_basic_content" name="dm_basic_content"></textarea>
				    </dd>
				</dl>
				<h3>게시판 상단/하단 꾸미기</h3>
				<dl>
				    <dt><label for="gr_id">상단디자인<br>(Header)</label></dt>
				    <dd>
				    	<p class="noty mb10">게시판 리스트페이지 상단에 표출될 컨텐츠를 입력해주세요.</p>
				        <textarea id="dm_header_content" name="dm_header_content"></textarea>
				    </dd>
				</dl>
				<dl>
				    <dt><label for="gr_id">하단디자인<br>(Footer)</label></dt>
				    <dd>
				    	<p class="noty mb10">게시판 리스트페이지 하단에 표출될 컨텐츠를 입력해주세요.</p>
				        <textarea id="dm_footer_content" name="dm_footer_content"></textarea>
				    </dd>
				</dl>
			</div>
		</form>
	</div>	
</div>
<script type="text/javascript">
var group_str = ["list","read","write","reply","comment","link","upload"];
var oEditors = [];
var oEditors1 = [];
var oEditors2 = [];
$(function (){
	<c:if test="${type eq 'insert'}">
		$(".useMain").hide();
	</c:if>
	
	nhn.husky.EZCreator.createInIFrame({ 
    	oAppRef : oEditors, 
    	elPlaceHolder : "dm_basic_content", //저는 textarea의 id와 똑같이 적어줬습니다.
    	sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
    	fCreator : "createSEditor2",
    	htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseModeChanger : true 
    	},
    	fOnAppLoad : function() {
    		oEditors.getById["dm_basic_content"].exec("SET_CONTENTS", [$("#dm_basic_content").val()]);
    	}
    });
    
    nhn.husky.EZCreator.createInIFrame({ 
    	oAppRef : oEditors1, 
    	elPlaceHolder : "dm_header_content", //저는 textarea의 id와 똑같이 적어줬습니다.
    	sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
    	fCreator : "createSEditor2",
    	htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseModeChanger : true 
    	},
    	fOnAppLoad : function() {
    		oEditors1.getById["dm_header_content"].exec("SET_CONTENTS", [$("#dm_header_content").val()]);
    	}
    });
    
    nhn.husky.EZCreator.createInIFrame({ 
    	oAppRef : oEditors2, 
    	elPlaceHolder : "dm_footer_content", //저는 textarea의 id와 똑같이 적어줬습니다.
    	sSkinURI : "/js/egovframework/diam/se2/SmartEditor2Skin.html", //경로를 꼭 맞춰주세요!
    	fCreator : "createSEditor2",
    	htParams : { // 툴바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseToolbar : true, // 입력창 크기 조절바 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseVerticalResizer : true, // 모드 탭(Editor | HTML | TEXT) 사용 여부 (true:사용/ false:사용하지 않음)
    	bUseModeChanger : true 
    	},
    	fOnAppLoad : function() {
    		oEditors2.getById["dm_footer_content"].exec("SET_CONTENTS", [$("#dm_footer_content").val()]);
    	}
    });
	
    $.ajax({
		url : "/adm/get_group_list.do",
		dataType : "json",
		async: false,
		success : function(data) {
			$(".group_list").empty();
			if (data.rows.length > 0) {
				for (var i=0 ; i<group_str.length ; i++) {
            		$.each(data.rows, function (key, value) {
            			$("#"+group_str[i]+"_group").append(
            				'<label><input type="checkbox" id="dm_'+group_str[i]+'_group_arr" name="dm_'+group_str[i]+'_group_arr" value="'+value.dm_group_id+'">'+value.dm_group_name+'</label>'
            			);
            		});
            	}
            } else {
            	for (var i=0 ; i<group_str.length ; i++) {
            		$("#"+group_str[i]+"_group").append(
           				'<p class="noty">등록된 그룹이 없습니다. 관리정보 > 그룹 관리에서 그룹을 등록해 주세요.</p>'
           			);            		
            	}
            }
		}, error: function(request, status, error) {
			if (request.status == "303") {
				location.replace("/adm/login.do");
			} else {
				$.messager.alert("error", request.responseJSON.notice, "warning");
			}
        }
	});
	
	// dm_id가 있을 경우(수정 버튼 클릭 시)
	var dm_id = $("#dm_id").val();
	if (dm_id > 0 && dm_id != null) {
    	$.ajax({
	        url : "/adm/get_board.do?dm_id="+dm_id,
	        dataType : "json",
	        async: false,
	        success : function (data) {
	            if(data.result == "success") {
	                setBbsData(data.rows);
	            } else {
	            	window.opener.$.messager.alert('경고', data.notice, 'warning');
	                self.close();
	            }
	        }, error:function(request,status,error) {
	        	if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
		        	window.opener.$.messager.alert('경고',request.responseJSON.notice,'warning');
		        	self.close();
				}
	        }
       });
	}
	    
    $("#dm_use_category").off().on('click', function () {
        if ($(this).is(":checked")) {
            $(this).parent().siblings().show();
        } else {
            $(this).parent().siblings().hide();
        }
    });

    $("#dm_is_hit").off().on('click', function () {
        if ($(this).is(":checked")) {
            $(this).parent().siblings("p").show();
        } else {
            $(this).parent().siblings("p").hide();
        }
    });
    
    $("#dm_is_new").off().on('click', function () {
        if ($(this).is(":checked")) {
            $(this).parent().siblings("p").show();
        } else {
            $(this).parent().siblings("p").hide();
        }
    });    
});
//분류 특수문자 유효성검사
function categoryCheck(obj) {
	var regExp = /[ \{\}\[\]\/?.;:|\)*~`!^\-_+┼<>@\#$%&\'\"\\\(\=]/gi;
	if(regExp.test(obj.value)) {
		obj.value = obj.value.substring( 0 , obj.value.length - 1 );
	}
}

// 데이터 셋팅
function setBbsData(row) {
	$("#dm_id").val(row.dm_id);
	$("#dm_basic_content").val(row.dm_basic_content);
	$("#dm_header_content").val(row.dm_header_content);
	$("#dm_footer_content").val(row.dm_footer_content);
	$("#dm_table").val(row.dm_table).attr('readonly', true);
	$("#dm_subject").val(unescapeHtml(row.dm_subject));
	$("#dm_writer_secret").val(row.dm_writer_secret);
	$("#dm_hit_count").val(row.dm_hit_count);
	$("#dm_hit_max").val(row.dm_hit_max);
	$("#dm_page_rows").val(row.dm_page_rows);
    $("#dm_upload_count").val(row.dm_upload_count);
    $("#dm_gallery_height").val(row.dm_gallery_height);
    $("#dm_gallery_width").val(row.dm_gallery_width);
    $("#dm_new_time").val(row.dm_new_time);
    $("#dm_category_list").val(row.dm_category_list);
    
    $("input[name=dm_main_use]:input[value="+row.dm_main_use+"]").prop("checked", true);
    if (row.dm_main_use == "0") {
    	$(".useMain").hide();
	}
    $("#dm_main_order").val(row.dm_main_order);
    $("#dm_main_count").val(row.dm_main_count);
    
    $("#dm_domain").val(row.dm_domain);
	$("#dm_domain_text").text(row.dm_domain_text);
	
    $("#dm_skin").combobox('reload', "/adm/select_board_skin.do?selected="+row.dm_skin);
  	   	
	$("input:radio[name='dm_use_secret']:radio[value='"+row.dm_use_secret+"']").prop('checked', true);
    $("input:radio[name='dm_writer_type']:radio[value='"+row.dm_writer_type+"']").prop('checked', true);
    $("input:radio[name='dm_reply_delete_type']:radio[value='"+row.dm_reply_delete_type+"']").prop('checked', true);
    $("input:radio[name='dm_use_comment_secret']:radio[value='"+row.dm_use_comment_secret+"']").prop('checked', true);
    $("input:radio[name='dm_auth_type']:radio[value='"+row.dm_auth_type+"']").prop('checked', true);
    
    (row.dm_use_list_category == "1") ? $("#dm_use_list_category").prop('checked', true) : $("#dm_use_list_category").prop('checked', false);
    (row.dm_use_dhtml_editor == "1") ? $("#dm_use_dhtml_editor").prop('checked', true) : $("#dm_use_dhtml_editor").prop('checked', false);
    (row.dm_is_reply == "1") ? $("#dm_is_reply").prop('checked', true) : $("#dm_is_reply").prop('checked', false);
    (row.dm_is_comment == "1") ? $("#dm_is_comment").prop('checked', true) : $("#dm_is_comment").prop('checked', false);
    (row.dm_is_hit == "1") ? $("#dm_is_hit").prop('checked', true) : $("#dm_is_hit").prop('checked', false);
    (row.dm_is_new == "1") ? $("#dm_is_new").prop('checked', true) : $("#dm_is_new").prop('checked', false);
    (row.dm_use_link == "1") ? $("#dm_use_link").prop('checked', true) : $("#dm_use_link").prop('checked', false);
    (row.dm_use_file == "1") ? $("#dm_use_file").prop('checked', true) : $("#dm_use_file").prop('checked', false);
    (row.dm_use_file_icon == "1") ? $("#dm_use_file_icon").prop('checked', true) : $("#dm_use_file_icon").prop('checked', false);
    
    var level_val_arr = new Array();
    level_val_arr.push(row.dm_list_level);
    level_val_arr.push(row.dm_read_level);
    level_val_arr.push(row.dm_write_level);
    level_val_arr.push(row.dm_reply_level);
    level_val_arr.push(row.dm_comment_level);
    level_val_arr.push(row.dm_link_level);
    level_val_arr.push(row.dm_upload_level);
	    
    for (var i=0 ; i<group_str.length ; i++) {
		$('input:radio[name=dm_'+group_str[i]+'_level]:input[value='+level_val_arr[i]+']').attr("checked", true);
    }
    
    var group_val_arr = new Array();
    group_val_arr.push(row.dm_list_group_arr);
    group_val_arr.push(row.dm_read_group_arr);
    group_val_arr.push(row.dm_write_group_arr);    
    group_val_arr.push(row.dm_reply_group_arr);
    group_val_arr.push(row.dm_comment_group_arr);
    group_val_arr.push(row.dm_link_group_arr);
    group_val_arr.push(row.dm_upload_group_arr);
    
    for (var i=0 ; i<group_str.length ; i++) {
    	var tmp_var = new Array();
    	tmp_var = group_val_arr[i];
    	if (tmp_var.length > 0) {
    		for (var j=0; j<tmp_var.length; j++) {
    			$("input:checkbox[name='dm_"+group_str[i]+"_group_arr'][value='"+tmp_var[j]+"']").prop('checked', true);
            }
        }
    }   
        
    if (row.dm_is_hit == "1") {
        $("#dm_is_hit").parent().siblings("p").show();
    }

    if (row.dm_hit_icon != "" && row.dm_hit_icon != null && row.dm_is_hit == "1") {
    	var hit_icon_arr = row.dm_hit_icon.split("/");
    	$("#hit_file").append(
            '<img src="'+row.dm_hit_icon+'"><a href="/adm/get_board_icon_download.do?file_name='+row.dm_hit_icon+'&amp;ori_file_name='+hit_icon_arr[5]+'" class="btn">다운로드</a> <label><input type="checkbox" name="dm_del_hit" id="dm_del_hit" value="1">삭제</label>'
        );
    }
    
    if (row.dm_is_new == "1") {
        $("#dm_is_new").parent().siblings("p").show();
    }
    
    if (row.dm_new_icon != "" && row.dm_new_icon != null && row.dm_is_new == "1") {
    	var new_icon_arr = row.dm_new_icon.split("/");
        $("#new_file").append(
            '<img src="'+row.dm_new_icon+'"><a href="/adm/get_board_icon_download.do?file_name='+row.dm_new_icon+'&amp;ori_file_name='+new_icon_arr[5]+'" class="btn">다운로드</a> <label><input type="checkbox" name="dm_del_new" id="dm_del_new" value="1">삭제</label>'
        );
    }
    
    if (row.dm_use_category == "1") {
        $("#dm_use_category").prop('checked', true);
        $("#dm_use_category").parent().siblings().show();
    } else {
        $("#dm_use_category").prop('checked', false);
        $("#dm_use_category").parent().siblings().hide();
    }
}

// 게시판 생성
function createNewBoard(){	
	if(confirm("정말로 저장하시겠습니까?")){
		
		if ($.trim($("#dm_table").val()) == "") {
			$.messager.alert("경고", "게시판 아이디를 입력하세요.", "warning");
			return;
		}
		if ($.trim($("#dm_subject").val()) == "") {
			$.messager.alert("경고", "게시판명을 입력하세요.", "warning");
			return;
		}
		if ($.trim($("#dm_hit_count").val()) == "") {
			$.messager.alert("경고", "조회당 Hit증가수를 입력하세요.", "warning");
			return;
		}
		if ($.trim($("#dm_hit_max").val()) == "") {
			$.messager.alert("경고", "HIT 아이콘 조회수를 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_new_time").val()) == "") {
			$.messager.alert("경고", "게시글 등록 후 NEW 아이콘 표출 시간을 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_page_rows").val()) == "") {
			$.messager.alert("경고", "페이지당 게시물 수를 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_gallery_width").val()) == "") {
			$.messager.alert("경고", "썸네일 이미지폭을 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_gallery_height").val()) == "") {
			$.messager.alert("경고", "썸네일 이미지높이를 입력해주세요.", "warning");
			return;
		}
		if ($.trim($("#dm_upload_count").val()) == "") {
			$.messager.alert("경고", "첨부파일 갯수를 입력해주세요.", "warning");
			return;
		}
		
	    
	    if ($("input[name=dm_main_use]:checked").val() == 0) {
			$("#dm_main_count").val(0);
			$("#dm_main_order").val(1);
		} else {
			var count = $("#dm_main_count").val();
			var order = $("#dm_main_order").val();
			
			if (order == "" || order < 1) {
				$.messager.alert('경고', "게시판 노출 순서 값이 올바르지 않습니다.","warning");
				$(".btnWrap").show();
				return;
			}
			if (count == "") {
				$.messager.alert('경고', "게시글 노출 건수를 입력하세요.","warning");
				$(".btnWrap").show();
				return;
			}
		}
	    
	    
		$(".btnWrap").hide();
		oEditors.getById["dm_basic_content"].exec("UPDATE_CONTENTS_FIELD", []); //textarea의 id를 적어줍니다.
	    oEditors1.getById["dm_header_content"].exec("UPDATE_CONTENTS_FIELD", []); //textarea의 id를 적어줍니다.
	    oEditors2.getById["dm_footer_content"].exec("UPDATE_CONTENTS_FIELD", []); //textarea의 id를 적어줍니다.
		var form = $("#fm")[0];
	    var formData = new FormData(form);
	    $.ajax({
	        url : "/adm/set_board.do",
	        type : 'POST',
	        data : formData,
	        contentType : false,
	        processData : false,
	        dataType : 'json',
	        success:function(data) {
	            if(data.result == "success") {
	            	window.opener.$.messager.alert('알림', data.notice, 'info');
	            	window.opener.$("#dg_data").datagrid('reload');
	                self.close();
	            } else {
	            	$.messager.alert('경고', data.notice, 'warning');
	                $(".btnWrap").show();
	            }
	        },
	        error:function(request, status, error) {
	        	if (request.status == "303") {
					location.replace("/adm/login.do");
				} else {
		        	$.messager.alert('경고',request.responseJSON.notice,'warning');
		            $(".btnWrap").show();
				}
	        }
	    });
	}
}

function cancleTheCurrentOperation(){
	if(confirm("현재 진행 중인 작업이 있습니다. 정말 취소하시겠습니까?")){
		self.close();
	}
}

$(function(){
	
	$("input[name=dm_main_use]").on("change", function(){
		var mainUse = $(this).val();
		if (mainUse == 1) {
			$(".useMain").show();
		} else {
			$(".useMain").hide();
		}
	});
	
});
</script>
<c:import url="/adm/page_bottom.do" />