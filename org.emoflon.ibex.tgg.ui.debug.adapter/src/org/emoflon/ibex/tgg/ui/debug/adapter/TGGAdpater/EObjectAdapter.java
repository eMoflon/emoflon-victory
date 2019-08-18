package org.emoflon.ibex.tgg.ui.debug.adapter.TGGAdpater;

import java.util.List;
import java.util.stream.Collectors;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.emoflon.ibex.tgg.ui.debug.api.IAttribute;
import org.emoflon.ibex.tgg.ui.debug.api.IObject;

public class EObjectAdapter implements IObject {
	private EObject eObject;
	public EObjectAdapter(EObject eObject) {
		super();
		this.eObject = eObject;
	}
	public EObject unWrap(){
		return this.eObject;
	}

	@Override
	public String indexFor(IObject object) {
		EObject obj = ((EObjectAdapter)object).unWrap();
		if (obj.eContainer() == null) {
			Resource resource = obj.eResource();
			return resource.getResourceSet().getResources().indexOf(resource) + "_" + resource.getContents().indexOf(obj);		}
		else {
			EObject container = obj.eContainer();
			return indexFor(new EObjectAdapter(container)) + "_" + container.eContents().indexOf(obj);
		}

	}

	@Override
	public String labelFor(IObject object) {
		EObject obj = ((EObjectAdapter)object).unWrap();
		if(obj.eContainingFeature() != null) {
			return obj.eContainingFeature().getName();
		} else {
			return "root";
		}

	}

	@Override
	public List<IAttribute> getClassAttribute() {

		return eObject.eClass().getEAttributes()//
				.stream()//
				.map(m -> new EAttributteAdapter(m))
				.collect(Collectors.toList());
	}

	@Override
	public Object getFeature(IAttribute attr) {

		return eObject.eGet(((EAttributteAdapter)attr).unWrap());
	}

	@Override
	public List<IObject> getContents() {

		return eObject.eContents()//
				.stream()//
				.map(m -> new EObjectAdapter(m))
				.collect(Collectors.toList());
	}
	@Override
	public String getContainingFeatureName() {

		return eObject.eContainingFeature().getName();
	}
	@Override
	public String getClassName() {

		return eObject.eClass().getName();
	}


}
