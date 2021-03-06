package com.thingtrack.konekti.view.web.form.field;

import java.util.List;

import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

import com.thingtrack.konekti.domain.Area;
import com.thingtrack.konekti.service.api.AreaService;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.ui.AbstractField;
import com.thingtrack.konekti.view.kernel.IWorkbenchContext;
import com.vaadin.annotations.AutoGenerated;
import com.vaadin.data.Property;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
public class ProductAreaCollectionField extends AbstractField {

	@AutoGenerated
	private VerticalLayout mainLayout;
	@AutoGenerated
	private Table dgProductArea;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */
	
	private BindingSource<Area> areasBindingSource = new BindingSource<Area>(Area.class);
	
	private List<Area> assignedArea;

	private AreaService areaService;

	private IWorkbenchContext context;
	
	/**
	 * The constructor should first build the main layout, set the
	 * composition root and then do any custom initialization.
	 *
	 * The constructor will not be automatically regenerated by the
	 * visual editor.
	 */
	public ProductAreaCollectionField(IWorkbenchContext context) {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		this.context = context;
		
		// retrieve enterprise services
		getServices();

		// configure datagrid
		dgProductArea.setContainerDataSource(areasBindingSource);
		dgProductArea.addGeneratedColumn(ProductAssignmentColumn.PRODUCT_COLUMN_ID, new ProductAssignmentColumn());
		dgProductArea.setVisibleColumns(new String[]{"code", "description", ProductAssignmentColumn.PRODUCT_COLUMN_ID});
		dgProductArea.setColumnHeaders(new String[] { "Code", "Descripción", "Activo" } );
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void setPropertyDataSource(Property newDataSource) {
		if(newDataSource.getValue() instanceof List) {			
			List<Area> areas = (List<Area>) newDataSource.getValue();
			
			//Already organziations assigned to parent vehicle
			assignedArea = areas;
			
			//Clean first the biding source
			areasBindingSource.removeAllItems();
			try {
				//Add all available organizations 
				//areasBindingSource.addAll(areaService.getAll(context.getUser()));
				areasBindingSource.addAll(areaService.getAllByOrganization(context.getUser()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		super.setPropertyDataSource(newDataSource);
	}
	
	@Override
	public Class<?> getType() {
		if(getPropertyDataSource() instanceof Property)
			return getPropertyDataSource().getType();
		
		return List.class;
	}
	
	@Override
	public Object getValue() {		
		return assignedArea;
		
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
		
		// Organizationstable
		dgProductArea = new Table();
		dgProductArea.setImmediate(false);
		dgProductArea.setWidth("100.0%");
		dgProductArea.setHeight("100.0%");
		mainLayout.addComponent(dgProductArea);
		mainLayout.setExpandRatio(dgProductArea, 1.0f);
		
		return mainLayout;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void getServices(){
		
		BundleContext bundleContext = FrameworkUtil.getBundle(this.getClass()).getBundleContext();
		
		ServiceReference areaServiceReference = bundleContext.getServiceReference(AreaService.class.getName());
		areaService = AreaService.class.cast(bundleContext.getService(areaServiceReference));
		
	}
	
	
	private class ProductAssignmentColumn implements Table.ColumnGenerator{

		static final String PRODUCT_COLUMN_ID = "area-assignment";

		@Override
		public Object generateCell(Table source, Object itemId, Object columnId) {
			
			CheckBox assignmentColumn = new CheckBox();
			
			final Area areaItem = (Area) itemId;
			
			assignmentColumn.setValue(assignedArea.contains(areaItem));
				
			assignmentColumn.addListener(new ValueChangeListener() {
				
				@Override
				public void valueChange(Property.ValueChangeEvent event) {
				
					boolean assignmentValue = (Boolean) event.getProperty().getValue();
					
					if(assignmentValue)
						assignedArea.add(areaItem);
					
					else assignedArea.remove(areaItem);
					
				}
				
			});
			
			return assignmentColumn;
		}
		
		
	}

	@Override
	protected void updateLabels() {				
		dgProductArea.setColumnHeaders(new String[] { getI18N().getMessage("com.thingtrack.konekti.view.web.form.field.ProductAreaCollectionField.dgProductArea.column.code"), 
													  getI18N().getMessage("com.thingtrack.konekti.view.web.form.field.ProductAreaCollectionField.dgProductArea.column.description"), 
													  getI18N().getMessage("com.thingtrack.konekti.view.web.form.field.ProductAreaCollectionField.dgProductArea.column.product")});
		
	}
	
}
