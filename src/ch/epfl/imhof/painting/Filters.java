package ch.epfl.imhof.painting;

import java.util.Arrays;
import java.util.function.Predicate;

import ch.epfl.imhof.Attributed;

public final class Filters{


	private Filters(){
	};

	public static Predicate<Attributed<?>> tagged(String attName){
		return (hasAttribute) -> {
			return hasAttribute.hasAttribute(attName);			
		};
	}

	public static Predicate<Attributed<?>> tagged(String attName, String... argValue){
		return (hasAttribute) -> {
			return (hasAttribute.hasAttribute(attName) && Arrays.asList(argValue).contains(hasAttribute.attributeValue(attName)));
		};
	}

	public static Predicate<Attributed<?>> onLayer(int layer){
		return (entity) -> {
			return entity.attributeValue("layer", 0)==layer;
		};
	}
}
