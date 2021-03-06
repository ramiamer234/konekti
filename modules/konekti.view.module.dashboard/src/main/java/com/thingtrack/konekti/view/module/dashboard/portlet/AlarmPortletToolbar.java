package com.thingtrack.konekti.view.module.dashboard.portlet;

import java.io.Serializable;

import org.vaadin.peter.buttongroup.ButtonGroup;

import com.thingtrack.konekti.domain.Alarm;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.data.BindingSource.IndexChangeEvent;
import com.thingtrack.konekti.view.addon.data.BindingSource.IndexChangeListener;
import com.thingtrack.konekti.view.addon.ui.AbstractToolbar;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.terminal.gwt.client.MouseEventDetails;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;

@SuppressWarnings("serial")
public class AlarmPortletToolbar extends AbstractToolbar {
	@AutoGenerated
	private HorizontalLayout toolbarLayout;

	@AutoGenerated
	private Button btnAlarmRefresh;
	@AutoGenerated
	private Button btnAlarmConfirmation;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	private Object register;	
	private int position = 0;
	
	// navigator button listeners
	private ClickConfirmAlarmButtonListener listenerConfirmAlarmButton = null;
	private ClickRefreshAlarmButtonListener listenerRefreshAlarmButton = null;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */	
	public AlarmPortletToolbar(int position, final BindingSource<?> bindingSource) {
		super(position, bindingSource);
			
		buildMainLayout();
		//setCompositionRoot(mainLayout);

		// TODO add user code here
		this.position = position;
		
		setBindingSource(bindingSource);
		
		btnAlarmConfirmation.setDescription("Confirmar Alarma");
		
		// set reject button listener
		btnAlarmRefresh.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				int index = bindingSource.getIndex();
				
				if (index == 0)
					return;
				
				Alarm alarmSelected = (Alarm) bindingSource.getItemId();
				
				if (listenerRefreshAlarmButton != null)
					listenerRefreshAlarmButton.refreshAlarmButtonClick(new ClickNavigationEvent(event.getButton(), event.getComponent() , null, alarmSelected, 0));					
				
			}
		});
				
		btnAlarmConfirmation.addListener(new ClickListener() {			
			public void buttonClick(ClickEvent event) {
				int index = bindingSource.getIndex();
				
				if (index == 0)
					return;
				
				Alarm alarmSelected = (Alarm) bindingSource.getItemId();
				
				if (listenerConfirmAlarmButton != null)
					listenerConfirmAlarmButton.confirmAlarmButtonClick(new ClickNavigationEvent(event.getButton(), event.getComponent() , null, alarmSelected, 0));					
				
			}
		});
		
	}
	
	@Override
	public int getPosition() {
		return this.position;
		
	}

	@Override
	public ComponentContainer getContent() {
		return this.toolbarLayout;
		
	}
	
	public void addListenerRefreshAlarmButton(ClickRefreshAlarmButtonListener listener) {
		this.listenerRefreshAlarmButton = listener;
		
	}
	public void addListenerConfirmAlarmButton(ClickConfirmAlarmButtonListener listener) {
		this.listenerConfirmAlarmButton = listener;
		
	}
	
	public interface ClickRefreshAlarmButtonListener extends Serializable {
        public void refreshAlarmButtonClick(ClickNavigationEvent event);

    }
	public interface ClickConfirmAlarmButtonListener extends Serializable {
        public void confirmAlarmButtonClick(ClickNavigationEvent event);

    }
	
	public class ClickNavigationEvent extends ClickEvent {
		private int index;
		private Object register;
		
		public ClickNavigationEvent(Button button, Component source) {
			button.super(source);
			
		}

		public ClickNavigationEvent(Button button, Component source, MouseEventDetails details) {
			button.super(source, details);
			
		}

		public ClickNavigationEvent(Button button, Component source, MouseEventDetails details, Object register, int index) {
			button.super(source, details);
			
			this.index = index;
			this.register = register;
		}

		public int getIndex() {
			return this.index;
			
		}
		
		public Object getRegister() {
			return this.register;
			
		}
		
	  }
	
	@Override
	public void setBindingSource(BindingSource<?> bindingSource) {
		this.bindingSource = bindingSource;
		
		// add change index binding source
		if (bindingSource != null) {
			bindingSource.addListenerToolBar((IndexChangeListener)this);
			
		}
		
	}
	
	@Override
	public void bindingSourceIndexChange(IndexChangeEvent event) {
		if (bindingSource != null) {
			Alarm alarmSelected = (Alarm)event.getRegister();
			
			if(alarmSelected == null)
				return;
		
		}
		
	}
	
	@AutoGenerated
	private void buildMainLayout() {
		toolbarLayout = buildToolbarLayout();
		addComponent(toolbarLayout);
		
	}
	
	@AutoGenerated
	private HorizontalLayout buildToolbarLayout() {		
		toolbarLayout = new HorizontalLayout();
		toolbarLayout.setImmediate(false);
		toolbarLayout.setSpacing(true);
		
		ButtonGroup editionButtonGroup = new ButtonGroup();
		toolbarLayout.addComponent(editionButtonGroup);
		
		// btnAlarmRefresh
		btnAlarmRefresh = new Button();
		btnAlarmRefresh.setCaption("Refrescar Alarmas");
		btnAlarmRefresh.setImmediate(true);
		btnAlarmRefresh.setWidth("-1px");
		btnAlarmRefresh.setHeight("-1px");
		btnAlarmRefresh.setIcon(new ThemeResource("../konekti/images/icons/navigation-toolbar/arrow-circle-double.png"));
		
		editionButtonGroup.addButton(btnAlarmRefresh);
		
		// btnAlarmConfirmation
		btnAlarmConfirmation = new Button();
		btnAlarmConfirmation.setCaption("Iniciar Job");
		btnAlarmConfirmation.setImmediate(true);
		btnAlarmConfirmation.setWidth("-1px");
		btnAlarmConfirmation.setHeight("-1px");
		btnAlarmConfirmation.setIcon(new ThemeResource("../konekti/images/icons/job-toolbar/bell--arrow.png"));
		
		editionButtonGroup.addButton(btnAlarmConfirmation);
				
		return toolbarLayout;
	}

	@Override
	protected void updateLabels() {
		btnAlarmConfirmation.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.module.alarm.addon.AlarmToolbar.btnAlarmConfirmation.caption"));
		
	}
}
