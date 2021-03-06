package com.thingtrack.konekti.view.addon.ui;

import org.vaadin.addon.formbinder.ViewBoundForm;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

import com.thingtrack.konekti.view.kernel.ui.layout.IForm;

@SuppressWarnings("serial")
public class AbstractBoundForm<BEANTYPE> extends CustomComponent implements IForm {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Panel pnFooterFormView;
	@AutoGenerated
	private HorizontalLayout hlFooterFormView;
	@AutoGenerated
	private Button btnCancel;
	@AutoGenerated
	private Button btnSave;
	@AutoGenerated
	private Panel pnBodyFormView;
	@AutoGenerated
	private VerticalLayout vlBodyFormView;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	private DialogResult dialogResult = DialogResult.IGNORE;
	
	private BEANTYPE domainEntity;
	
	  /**
	   * Possible results of the Window Dialog
	   * @param Abort
	   * @param Cancel -> dialog window result throw default Cancel window dialog button click
	   * @param Ignore
	   * @param No
	   * @param None
	   * @param Ok -> dialog window result throw default Ok window dialog button click
	   * @param Retry
	   * @param Yes
	  */
	  public enum DialogResult {
		  ABORT,
		  CANCEL,
		  IGNORE,
		  NO,
		  NONE,
		  OK,
		  RETRY,
		  YES
	  }
	  
	private String caption;
	private Window mainWindow;
	
	private ViewBoundForm viewBoundForm;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public AbstractBoundForm(Window mainWindow, String caption, CustomComponent formView) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.caption = caption;
		this.mainWindow = mainWindow;
		
		viewBoundForm = new ViewBoundForm(formView);
		
		btnSave.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				viewBoundForm.commit();
				
			}
		});
		
		btnCancel.addListener(new ClickListener() {			
			@Override
			public void buttonClick(ClickEvent event) {
				viewBoundForm.discard();
				
			}
		});
	}
	
	@Override
	public void setDomainEntity(Object domainEntity) {		
//		if(!(domainEntity instanceof Class<? super BEANTYPE>))
//			throw new ClassCastException("domainEntity is not a Organization bean");
		
		this.domainEntity = (BEANTYPE) domainEntity;
		
		viewBoundForm.setItemDataSource(new BeanItem<BEANTYPE>((BEANTYPE) domainEntity));
	}
	
	@Override 
	public Object getDomainEntity() {
		return (BEANTYPE) domainEntity;
		
	}
		
	@AutoGenerated
	private VerticalLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new VerticalLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("100%");
		mainLayout.setHeight("100%");
		mainLayout.setMargin(false);
		
		// top-level component properties
		setWidth("100.0%");
		setHeight("100.0%");
		
		// pnBodyFormView
		pnBodyFormView = buildPnBodyFormView();
		mainLayout.addComponent(pnBodyFormView);
		mainLayout.setExpandRatio(pnBodyFormView, 1.0f);
		
		// pnFooterFormView
		pnFooterFormView = buildPnFooterFormView();
		mainLayout.addComponent(pnFooterFormView);
		mainLayout.setComponentAlignment(pnFooterFormView, new Alignment(9));
		
		return mainLayout;
	}

	@AutoGenerated
	private Panel buildPnBodyFormView() {
		// common part: create layout
		pnBodyFormView = new Panel();
		pnBodyFormView.setImmediate(false);
		pnBodyFormView.setWidth("100.0%");
		pnBodyFormView.setHeight("100.0%");
		
		// vlBodyFormView
		vlBodyFormView = new VerticalLayout();
		vlBodyFormView.setImmediate(false);
		vlBodyFormView.setWidth("100.0%");
		vlBodyFormView.setHeight("100.0%");
		vlBodyFormView.setMargin(false);
		pnBodyFormView.setContent(vlBodyFormView);
		
		return pnBodyFormView;
	}

	@AutoGenerated
	private Panel buildPnFooterFormView() {
		// common part: create layout
		pnFooterFormView = new Panel();
		pnFooterFormView.setImmediate(false);
		pnFooterFormView.setWidth("100.0%");
		pnFooterFormView.setHeight("35px");
		
		// hlFooterFormView
		hlFooterFormView = buildHlFooterFormView();
		pnFooterFormView.setContent(hlFooterFormView);
		
		return pnFooterFormView;
	}

	@AutoGenerated
	private HorizontalLayout buildHlFooterFormView() {
		// common part: create layout
		hlFooterFormView = new HorizontalLayout();
		hlFooterFormView.setImmediate(false);
		hlFooterFormView.setWidth("100.0%");
		hlFooterFormView.setHeight("100.0%");
		hlFooterFormView.setMargin(false);
		
		// btnSave
		btnSave = new Button();
		btnSave.setCaption("Guardar");
		btnSave.setImmediate(false);
		btnSave.setWidth("-1px");
		btnSave.setHeight("-1px");
		hlFooterFormView.addComponent(btnSave);
		hlFooterFormView.setExpandRatio(btnSave, 1.0f);
		hlFooterFormView.setComponentAlignment(btnSave, new Alignment(10));
		
		// btnCancel
		btnCancel = new Button();
		btnCancel.setCaption("Cancel");
		btnCancel.setImmediate(false);
		btnCancel.setWidth("-1px");
		btnCancel.setHeight("-1px");
		hlFooterFormView.addComponent(btnCancel);
		hlFooterFormView.setComponentAlignment(btnCancel, new Alignment(10));
		
		return hlFooterFormView;
	}


}
