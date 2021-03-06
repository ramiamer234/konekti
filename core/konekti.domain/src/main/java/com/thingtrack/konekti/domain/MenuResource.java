package com.thingtrack.konekti.domain;

/*
 * #%L
 * Konekti Domain Layer
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2010 - 2014 Thingtrack s.l.
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.DiscriminatorType;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * Entity class
 * <p>
 * Menu Resource
 * <p>
 * @author Thingtrack S.L.
 *
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "MENU_RESOURCE")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("RESOURCE")
public class MenuResource implements Serializable {
	
	/**
	 * Unique identifier
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "MENU_RESOURCE_ID")
	private Integer menuResourceId;
		
	/**
	 * Position
	 */
	@Column(name = "POSITION")
	private int position;
	
	/**
	 * Caption 
	 */
	@Column(name = "CAPTION")
	private String caption;

	/**
	 * Key Caption 
	 */
	@Column(name = "KEY_CAPTION", unique=true)
	private String keyCaption;
	
	/**
	 * Key Hint 
	 */
	@Column(name = "KEY_HINT", unique=true)
	private String keyHint;
	
	/**
	 * Shortcut
	 */
	@Column(name = "SHORTCUT")
	private String shortCut;
	
	/**
	 * Icon picture in bytes
	 */
	@Column(name = "ICON")
	@Lob
	private byte[] icon;
	
	/**
	 * Visible
	 */
	@Column(name = "VISIBLE")
	private boolean visible;

	/**
	 * @return the menuResourceId
	 */
	public Integer getMenuResourceId() {
		return menuResourceId;
	}

	/**
	 * @param menuResourceId the menuResourceId to set
	 */
	public void setMenuResourceId(Integer menuResourceId) {
		this.menuResourceId = menuResourceId;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}
	
	/**
	 * @param caption the caption to set
	 */
	public void setCaption(String caption) {
		this.caption = caption;
	}

	/**
	 * @return the caption
	 */
	public String getCaption() {
		return caption;
	}
		
	/**
	 * @return the shortCut
	 */
	public String getShortCut() {
		return shortCut;
	}

	/**
	 * @param shortCut the shortCut to set
	 */
	public void setShortCut(String shortCut) {
		this.shortCut = shortCut;
	}
	
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(byte[] icon) {
		this.icon = icon;
	}

	/**
	 * @return the icon
	 */
	public byte[] getIcon() {
		return icon;
	}
	
	/**
	 * @return the visible
	 */
	public boolean isVisible() {
		return visible;
	}

	/**
	 * @param visible the visible to set
	 */
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.caption;
		
	}

	public String getKeyCaption() {
		return keyCaption;
	}

	public void setKeyCaption(String keyCaption) {
		this.keyCaption = keyCaption;
	}

	public String getKeyHint() {
		return keyHint;
	}

	public void setKeyHint(String keyHint) {
		this.keyHint = keyHint;
	}

}
