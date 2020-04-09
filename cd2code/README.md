# CD2Code

Provides a generator for transforming Class Diagrams into executable code.
The module uses a modified version of the CD4A grammar, introducing additional
attribute types.

Currently, only the generation of Java code is supported.

## Creator

Contains a collection different [Software Pattern](https://en.wikipedia.org/wiki/Software_design_pattern) 
that expand upon the existing class diagram.

###FactoryCreator

Applies the [Factory Pattern](https://en.wikipedia.org/wiki/Factory_method_pattern)
on every class that contains the stereotype "Factory". 

The factory class will consist of two static function. Both expect all essential
class attributes as arguments. Essential, in this context, refers to all attributes
that are not themselves container classes (e.g. lists, maps, optionals). Those
will be initialized as empty containers and have to be set separately.

Additionally, one of the methods will expect a supplier for the class instance, while
the other one will use the default class. This allows to include instances of superclasses.

###VisitorCreator

Applies the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern) 
on the class diagram.

The visitor will traverse through the class diagram in a top-down fashion and visit
all instances specified in this scope. In case those instances are wrapped around container
classes, the appropriate accessor is used (e.g. Iterator for collections, ifPresent for Optional).


##CD2Java

A concrete implementation of the generator for Java classes.

### Decorator

Adds additional methods to the individual classes. In addition to the corresponding
method for the Visitor pattern, it will also generate accessor methods for container classes.
For all other attributes, simple Getter and Setter are created.

### Template

A collection of transformation rules for the elements of the class diagram.

####AnnotatorTemplate

Annotates non-primitive types according to their stereotypes.
Currently, only Nullable and Nonnull are supported.

####DecoratorTemplate

Generates accessor methods for the individual class attributes.

####FactoryTemplate

Applies the factory pattern for all classes that have the "Factory" stereotype.

####HandwrittenFileTemplate

Applies the TOP mechanism to all handwritten classes.
i.e. if there already exists a class with the same name, "TOP" will be attached
to the name of this class.

####ImportTransformer

Binds the import statements of the class diagram to the class templates.

####InitializerTemplate

Initializes container attributes.

####OptionalUnwrapperTemplate

Replaces the type of Optional attributes by the type of the contained argument.
Optional.empty() is then represented by null. This is to avoid any issues with 
Serializability.

####PackageTransformer

Binds the package of the class diagram to the class templates.

####VisitorTemplate

Adds the accept method for the visitor pattern to the individual classes.