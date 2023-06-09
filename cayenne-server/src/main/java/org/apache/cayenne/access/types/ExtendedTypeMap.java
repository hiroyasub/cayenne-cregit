begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|access
operator|.
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArrayList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * Stores ExtendedTypes, implementing an algorithm to determine the right type  * for a given Java class. See {@link #getRegisteredType(String)} documentation  * for lookup algorithm details.  */
end_comment

begin_class
specifier|public
class|class
name|ExtendedTypeMap
block|{
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|classesForPrimitives
decl_stmt|;
static|static
block|{
name|classesForPrimitives
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"long"
argument_list|,
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"double"
argument_list|,
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"byte"
argument_list|,
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"boolean"
argument_list|,
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"float"
argument_list|,
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"short"
argument_list|,
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"int"
argument_list|,
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|classesForPrimitives
operator|.
name|put
argument_list|(
literal|"char"
argument_list|,
name|Character
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|typeAliases
decl_stmt|;
specifier|protected
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|ExtendedType
argument_list|>
name|typeMap
decl_stmt|;
specifier|protected
name|ExtendedType
name|defaultType
decl_stmt|;
name|Collection
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|extendedTypeFactories
decl_stmt|;
comment|// standard type factories registered by Cayenne that are consulted after the user factories.
name|Collection
argument_list|<
name|ExtendedTypeFactory
argument_list|>
name|internalTypeFactories
decl_stmt|;
comment|/** 	 * Creates new ExtendedTypeMap, populating it with default JDBC-compatible 	 * types. If JDK version is at least 1.5, also loads support for enumerated 	 * types. 	 */
specifier|public
name|ExtendedTypeMap
parameter_list|()
block|{
name|this
operator|.
name|defaultType
operator|=
operator|new
name|ObjectType
argument_list|()
expr_stmt|;
name|this
operator|.
name|typeMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|typeAliases
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|(
name|classesForPrimitives
argument_list|)
expr_stmt|;
name|this
operator|.
name|extendedTypeFactories
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|this
operator|.
name|internalTypeFactories
operator|=
operator|new
name|CopyOnWriteArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|initDefaultFactories
argument_list|()
expr_stmt|;
block|}
comment|/** 	 * Registers default factories for creating enum types and serializable 	 * types. Note that user-defined factories are consulted before any default 	 * factory. 	 *  	 * @since 3.0 	 */
specifier|protected
name|void
name|initDefaultFactories
parameter_list|()
block|{
name|internalTypeFactories
operator|.
name|add
argument_list|(
operator|new
name|EnumTypeFactory
argument_list|()
argument_list|)
expr_stmt|;
name|internalTypeFactories
operator|.
name|add
argument_list|(
operator|new
name|ByteOrCharArrayFactory
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
comment|// note that Serializable type should be used as a last resort after all
comment|// other alternatives are exhausted.
name|internalTypeFactories
operator|.
name|add
argument_list|(
operator|new
name|SerializableTypeFactory
argument_list|(
name|this
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Adds an ExtendedTypeFactory that will be consulted if no direct mapping 	 * for a given class exists. This feature can be used to map interfaces. 	 *<p> 	 *<i>Note that the order in which factories are added is important, as 	 * factories are consulted in turn when an ExtendedType is looked up, and 	 * lookup is stopped when any factory provides a non-null type.</i> 	 *</p> 	 *  	 * @since 1.2 	 */
specifier|public
name|void
name|addFactory
parameter_list|(
name|ExtendedTypeFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Attempt to add null factory"
argument_list|)
throw|;
block|}
name|extendedTypeFactories
operator|.
name|add
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Removes a factory from the registered factories if it was previously 	 * added. 	 *  	 * @since 1.2 	 */
specifier|public
name|void
name|removeFactory
parameter_list|(
name|ExtendedTypeFactory
name|factory
parameter_list|)
block|{
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|extendedTypeFactories
operator|.
name|remove
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
block|}
comment|/** 	 * Adds a new type to the list of registered types. If there is another type 	 * registered for a class described by the<code>type</code> argument, the 	 * old handler is overridden by the new one. 	 */
specifier|public
name|void
name|registerType
parameter_list|(
name|ExtendedType
name|type
parameter_list|)
block|{
name|typeMap
operator|.
name|put
argument_list|(
name|type
operator|.
name|getClassName
argument_list|()
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Returns a default ExtendedType that is used to handle unmapped types. 	 */
specifier|public
name|ExtendedType
name|getDefaultType
parameter_list|()
block|{
return|return
name|defaultType
return|;
block|}
comment|/** 	 * Returns a guaranteed non-null ExtendedType instance for a given Java 	 * class name. Primitive class names are internally replaced by the 	 * non-primitive counterparts. The following lookup sequence is used to 	 * determine the type: 	 *<ul> 	 *<li>First the methods checks for an ExtendedType explicitly registered 	 * with the map for a given class name (most common types are registered by 	 * Cayenne internally; users can register their own).</li> 	 *<li>Second, the method tries to obtain a type by iterating through 	 * {@link ExtendedTypeFactory} instances registered by users. If a factory 	 * returns a non-null type, it is returned to the user and the rest of the 	 * factories are ignored.</li> 	 *<li>Third, the method iterates through standard 	 * {@link ExtendedTypeFactory} instances that can dynamically construct 	 * extended types for serializable objects and JDK 1.5 enums.</li> 	 *<li>If all the methods above failed, the default type is returned that 	 * relies on default JDBC driver mapping to set and get objects.</li> 	 *</ul> 	 *<i>Note that for array types class name must be in the form 	 * 'MyClass[]'</i>. 	 */
specifier|public
name|ExtendedType
name|getRegisteredType
parameter_list|(
name|String
name|javaClassName
parameter_list|)
block|{
if|if
condition|(
name|javaClassName
operator|==
literal|null
condition|)
block|{
return|return
name|getDefaultType
argument_list|()
return|;
block|}
name|javaClassName
operator|=
name|canonicalizedTypeName
argument_list|(
name|javaClassName
argument_list|)
expr_stmt|;
name|ExtendedType
name|type
init|=
name|getExplictlyRegisteredType
argument_list|(
name|javaClassName
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|type
return|;
block|}
name|type
operator|=
name|createType
argument_list|(
name|javaClassName
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
comment|// register to speed up future access
name|registerType
argument_list|(
name|type
argument_list|)
expr_stmt|;
return|return
name|type
return|;
block|}
return|return
name|getDefaultType
argument_list|()
return|;
block|}
name|ExtendedType
name|getExplictlyRegisteredType
parameter_list|(
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Null className"
argument_list|)
throw|;
block|}
return|return
name|typeMap
operator|.
name|get
argument_list|(
name|className
argument_list|)
return|;
block|}
comment|/** 	 * Returns a type registered for the class name. If no such type exists, 	 * returns the default type. It is guaranteed that this method returns a 	 * non-null ExtendedType instance. 	 */
specifier|public
name|ExtendedType
name|getRegisteredType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|javaClass
parameter_list|)
block|{
return|return
name|getRegisteredType
argument_list|(
name|javaClass
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
return|;
block|}
comment|/** 	 * Removes registered ExtendedType object corresponding to 	 *<code>javaClassName</code> parameter. 	 */
specifier|public
name|void
name|unregisterType
parameter_list|(
name|String
name|javaClassName
parameter_list|)
block|{
name|typeMap
operator|.
name|remove
argument_list|(
name|javaClassName
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * Returns array of Java class names supported by Cayenne for JDBC mapping. 	 */
specifier|public
name|String
index|[]
name|getRegisteredTypeNames
parameter_list|()
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|keys
init|=
name|typeMap
operator|.
name|keySet
argument_list|()
decl_stmt|;
name|int
name|len
init|=
name|keys
operator|.
name|size
argument_list|()
decl_stmt|;
name|String
index|[]
name|types
init|=
operator|new
name|String
index|[
name|len
index|]
decl_stmt|;
name|Iterator
argument_list|<
name|String
argument_list|>
name|it
init|=
name|keys
operator|.
name|iterator
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|len
condition|;
name|i
operator|++
control|)
block|{
name|types
index|[
name|i
index|]
operator|=
name|it
operator|.
name|next
argument_list|()
expr_stmt|;
block|}
return|return
name|types
return|;
block|}
comment|/** 	 * Returns an ExtendedType for specific Java classes. Uses user-provided and 	 * Cayenne-provided {@link ExtendedTypeFactory} factories to instantiate the 	 * ExtendedType. All primitive classes must be converted to the 	 * corresponding Java classes by the callers. 	 *  	 * @return a default type for a given class or null if a class has no 	 *         default type mapping. 	 * @since 1.2 	 */
specifier|protected
name|ExtendedType
name|createType
parameter_list|(
name|String
name|className
parameter_list|)
block|{
if|if
condition|(
name|className
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Class
argument_list|<
name|?
argument_list|>
name|typeClass
decl_stmt|;
try|try
block|{
name|typeClass
operator|=
name|Util
operator|.
name|getJavaClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignore exceptions...
return|return
literal|null
return|;
block|}
comment|// lookup in user factories first
for|for
control|(
name|ExtendedTypeFactory
name|factory
range|:
name|extendedTypeFactories
control|)
block|{
name|ExtendedType
name|type
init|=
name|factory
operator|.
name|getType
argument_list|(
name|typeClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|type
return|;
block|}
block|}
comment|// lookup in internal factories
for|for
control|(
name|ExtendedTypeFactory
name|factory
range|:
name|internalTypeFactories
control|)
block|{
name|ExtendedType
name|type
init|=
name|factory
operator|.
name|getType
argument_list|(
name|typeClass
argument_list|)
decl_stmt|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|)
block|{
return|return
name|type
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/** 	 * For the class name returns a name "canonicalized" for the purpose of 	 * ExtendedType lookup. 	 *  	 * @since 4.0 	 */
specifier|protected
name|String
name|canonicalizedTypeName
parameter_list|(
name|String
name|className
parameter_list|)
block|{
name|String
name|canonicalized
init|=
name|typeAliases
operator|.
name|get
argument_list|(
name|className
argument_list|)
decl_stmt|;
if|if
condition|(
name|canonicalized
operator|==
literal|null
condition|)
block|{
name|int
name|index
init|=
name|className
operator|.
name|indexOf
argument_list|(
literal|'$'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>=
literal|0
condition|)
block|{
name|canonicalized
operator|=
name|className
operator|.
name|replace
argument_list|(
literal|'$'
argument_list|,
literal|'.'
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|canonicalized
operator|=
name|className
expr_stmt|;
block|}
name|typeAliases
operator|.
name|put
argument_list|(
name|className
argument_list|,
name|canonicalized
argument_list|)
expr_stmt|;
block|}
return|return
name|canonicalized
return|;
block|}
block|}
end_class

end_unit

