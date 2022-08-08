package com.ontimize.atomicHotelsApiRest.model.core.tools;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ControlPermissions {
	private List<String> roleUsersRestrictions;
	Map<String, Object> keyMap;
	
	public ControlPermissions() {
		reset();
	}
	
	public void setMap(Map<String, Object> keyMap) {
		
	}
	
	public void reset() {
		this.roleUsersRestrictions = null;
		this.keyMap = null;	
	}

	public void setRoleUsersRestrictions(String ...roleUsersRestrictions ) {
		this.roleUsersRestrictions = Arrays.asList(roleUsersRestrictions);
	}
	
	public void restrict(Map<String, Object> keyMap) {
		
	}
}
