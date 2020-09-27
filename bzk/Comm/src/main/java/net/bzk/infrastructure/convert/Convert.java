package net.bzk.infrastructure.convert;

@FunctionalInterface
public interface Convert<X, Y> {

	public Y convert(X input);

}