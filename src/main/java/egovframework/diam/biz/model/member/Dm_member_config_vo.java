package egovframework.diam.biz.model.member;

import java.io.Serializable;
import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Dm_member_config_vo implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2099858820139876111L;

	private String dm_id;
	
	private String dm_is_member;
	
	private String dm_use_nick;
	
	private String dm_require_nick;
	
	private String dm_use_sex;
	
	private String dm_require_sex;
	
	private String dm_use_birth;
	
	private String dm_require_birth;
	
	private String dm_use_email;
	
	private String dm_require_email;
	
	private String dm_use_homepage;
	
	private String dm_require_homepage;
	
	private String dm_use_hp;
	
	private String dm_require_hp;
	
	private String dm_use_addr;
	
	private String dm_require_addr;
	
	private String dm_use_tel;
	
	private String dm_require_tel;
	
	private String dm_use_introduce;
	
	private String dm_require_introduce;
	
	private String dm_use_recom;
	
	private String dm_require_recom;
	
	private String dm_create_dt;
	
	private String dm_create_id;
	
	private String dm_modify_dt;
	
	private String dm_modify_id;

}
