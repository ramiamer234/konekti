package com.thingtrack.konekti.view.addon.ui;

import java.util.Locale;

import com.github.peholmst.i18n4vaadin.I18N;
import com.github.peholmst.i18n4vaadin.I18NComponent;
import com.github.peholmst.i18n4vaadin.I18NListener;
import com.github.peholmst.i18n4vaadin.support.I18NComponentSupport;
import com.thingtrack.konekti.view.addon.data.BindingSource;
import com.thingtrack.konekti.view.addon.data.BindingSource.IndexChangeListener;
import com.thingtrack.konekti.view.addon.data.BindingSourceComponent;
import com.thingtrack.konekti.view.kernel.ui.layout.IToolbar;
import com.vaadin.ui.CssLayout;

@SuppressWarnings("serial")
public abstract class AbstractToolbar extends CssLayout implements IToolbar, BindingSourceComponent, IndexChangeListener, I18NComponent, I18NListener {
	protected int position;
	protected BindingSource<?> bindingSource;
	
	private final I18NComponentSupport support = new I18NComponentSupport(this);
	
	public AbstractToolbar(int position, BindingSource<?> bindingSource) {
		this.position = position;
		this.bindingSource = bindingSource;
	}
	
	@Override
	public BindingSource<?> getBindingSource() {
		return this.bindingSource;
		
	}

	@Override
	public void setBindingSource(BindingSource<?> bindingSource) {
		this.bindingSource = bindingSource;
		
	}
	
	@Override
	public void setI18N(I18N i18n) {
		support.setI18N(i18n);
	}

	@Override
	public I18N getI18N() {
		return support.getI18N();
	}
	
	@Override
	public void attach() {
		super.attach();
		getI18N().addListener(this);
		updateLabels();
	}

	@Override
	public void detach() {
		getI18N().removeListener(this);
		super.detach();
	}

	@Override
	public void localeChanged(I18N sender, Locale oldLocale, Locale newLocale)  {
		updateLabels();
	}
	
	protected abstract void updateLabels();
}
