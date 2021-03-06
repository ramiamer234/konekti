package com.thingtrack.konekti.view.web.form;

import com.thingtrack.konekti.view.addon.ui.AbstractViewForm;
import com.thingtrack.konekti.view.web.form.field.FileField;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

@SuppressWarnings("serial")
public class KnowledgeViewForm extends AbstractViewForm {

	@AutoGenerated
	private AbsoluteLayout mainLayout;
	@AutoGenerated
	private TextArea errorMessageField;
	@AutoGenerated
	private FileField fileField;
	@AutoGenerated
	private CheckBox activeField;
	@AutoGenerated
	private TextField versionField;
	@AutoGenerated
	private TextField packageNameField;
	@AutoGenerated
	private TextField nameField;
	@AutoGenerated
	private TextField idField;
	@AutoGenerated
	private TextField descriptionField;

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

	/**
	 * The constructor should first build the main layout, set the composition
	 * root and then do any custom initialization.
	 * 
	 * The constructor will not be automatically regenerated by the visual
	 * editor.
	 */
	public KnowledgeViewForm() {
		buildMainLayout();
		setCompositionRoot(mainLayout);

		// TODO add user code here
		idField.setNullRepresentation("");
		versionField.setNullRepresentation("");
		packageNameField.setNullRepresentation("");
		nameField.setNullRepresentation("");
		descriptionField.setNullRepresentation("");
		errorMessageField.setNullRepresentation("");
		
		fileField.setRequired(true);		
		
	}
	
	@AutoGenerated
	private AbsoluteLayout buildMainLayout() {
		// common part: create layout
		mainLayout = new AbsoluteLayout();
		mainLayout.setImmediate(false);
		mainLayout.setWidth("580px");
		mainLayout.setHeight("360px");
		mainLayout.setMargin(true);
		
		// top-level component properties
		setWidth("580px");
		setHeight("360px");
		
		// descriptionField
		descriptionField = new TextField();
		descriptionField.setCaption("Descripción");
		descriptionField.setImmediate(false);
		descriptionField.setWidth("380px");
		descriptionField.setHeight("-1px");
		mainLayout.addComponent(descriptionField, "top:112.0px;left:180.0px;");
		
		// idField
		idField = new TextField();
		idField.setCaption("Id");
		idField.setImmediate(false);
		idField.setWidth("400px");
		idField.setHeight("-1px");
		idField.setRequired(true);
		mainLayout.addComponent(idField, "top:20.0px;left:20.0px;");
		
		// nameField
		nameField = new TextField();
		nameField.setCaption("Nombre");
		nameField.setImmediate(false);
		nameField.setWidth("140px");
		nameField.setHeight("-1px");
		nameField.setRequired(true);
		mainLayout.addComponent(nameField, "top:112.0px;left:20.0px;");
		
		// packageNameField
		packageNameField = new TextField();
		packageNameField.setCaption("Paquete");
		packageNameField.setImmediate(false);
		packageNameField.setWidth("400px");
		packageNameField.setHeight("-1px");
		packageNameField.setRequired(true);
		mainLayout.addComponent(packageNameField, "top:64.0px;left:20.0px;");
		
		// versionField
		versionField = new TextField();
		versionField.setCaption("Versión");
		versionField.setImmediate(false);
		versionField.setWidth("120px");
		versionField.setHeight("-1px");
		versionField.setRequired(true);
		mainLayout.addComponent(versionField, "top:64.0px;left:440.0px;");
		
		// activeField
		activeField = new CheckBox();
		activeField.setCaption("Activo");
		activeField.setImmediate(false);
		activeField.setWidth("-1px");
		activeField.setHeight("-1px");
		activeField.setRequired(true);
		mainLayout.addComponent(activeField, "top:20.0px;left:507.0px;");
		
		// fileField
		fileField = new FileField();
		fileField.setImmediate(false);
		fileField.setWidth("540px");
		fileField.setHeight("-1px");
		mainLayout.addComponent(fileField, "top:160.0px;left:20.0px;");
		
		// errorMessageField
		errorMessageField = new TextArea();
		errorMessageField.setCaption("Mensaje Error");
		errorMessageField.setImmediate(false);
		errorMessageField.setWidth("540px");
		errorMessageField.setHeight("120px");
		mainLayout.addComponent(errorMessageField, "top:220.0px;left:20.0px;");
		
		return mainLayout;
	}

	@Override
	protected void updateLabels() {
		fileField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.fileField.caption"));
		activeField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.activeField.caption"));
		versionField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.versionField.caption"));
		packageNameField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.packageNameField.caption"));
		nameField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.nameField.caption"));
		idField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.idField.caption"));
		descriptionField.setCaption(getI18N().getMessage("com.thingtrack.konekti.view.web.form.KnowledgeViewForm.descriptionField.caption"));
		
		errorMessageField.setReadOnly(true);
	}
}
