begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    http://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|gen
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
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
name|List
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
comment|/**  * Methods for mangling strings.  *   * @author Mike Kienenberger  */
end_comment

begin_class
specifier|public
class|class
name|ImportUtils
block|{
specifier|public
specifier|static
specifier|final
name|String
name|importOrdering
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"java."
block|,
literal|"javax."
block|,
literal|"org."
block|,
literal|"com."
block|}
decl_stmt|;
specifier|static
specifier|final
name|String
name|primitives
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"long"
block|,
literal|"double"
block|,
literal|"byte"
block|,
literal|"boolean"
block|,
literal|"float"
block|,
literal|"short"
block|,
literal|"int"
block|}
decl_stmt|;
specifier|static
specifier|final
name|String
name|primitiveClasses
index|[]
init|=
operator|new
name|String
index|[]
block|{
name|Long
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Double
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Byte
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Float
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Short
operator|.
name|class
operator|.
name|getName
argument_list|()
block|,
name|Integer
operator|.
name|class
operator|.
name|getName
argument_list|()
block|}
decl_stmt|;
specifier|static
name|Map
name|classesForPrimitives
init|=
name|Util
operator|.
name|toMap
argument_list|(
name|primitives
argument_list|,
name|primitiveClasses
argument_list|)
decl_stmt|;
specifier|static
name|Map
name|primitivesForClasses
init|=
name|Util
operator|.
name|toMap
argument_list|(
name|primitiveClasses
argument_list|,
name|primitives
argument_list|)
decl_stmt|;
specifier|protected
name|Map
name|importTypesMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|protected
name|Map
name|reservedImportTypesMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
comment|// Types forced to be FQN
specifier|protected
name|String
name|packageName
decl_stmt|;
specifier|public
name|ImportUtils
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|boolean
name|canRegisterType
parameter_list|(
name|String
name|typeName
parameter_list|)
block|{
comment|// Not sure why this would ever happen, but it did
if|if
condition|(
literal|null
operator|==
name|typeName
condition|)
return|return
literal|false
return|;
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|typeClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
name|String
name|typePackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|typePackageName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
return|return
literal|false
return|;
comment|// disallow non-packaged types (primatives, probably)
if|if
condition|(
literal|"java.lang"
operator|.
name|equals
argument_list|(
name|typePackageName
argument_list|)
condition|)
return|return
literal|false
return|;
comment|// Can only have one type -- rest must use fqn
if|if
condition|(
name|reservedImportTypesMap
operator|.
name|containsKey
argument_list|(
name|typeClassName
argument_list|)
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|importTypesMap
operator|.
name|containsKey
argument_list|(
name|typeClassName
argument_list|)
condition|)
return|return
literal|false
return|;
return|return
literal|true
return|;
block|}
comment|/**      * Reserve a fully-qualified data type class name so it cannot be used by another class.      * No import statements will be generated for reserved types.      * Typically, this is the fully-qualified class name of the class being generated.      * @param typeName FQ data type class name.      */
specifier|public
name|void
name|addReservedType
parameter_list|(
name|String
name|typeName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|canRegisterType
argument_list|(
name|typeName
argument_list|)
condition|)
return|return;
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|typeClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
name|reservedImportTypesMap
operator|.
name|put
argument_list|(
name|typeClassName
argument_list|,
name|typeName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Register a fully-qualified data type class name.      * For example, org.apache.cayenne.CayenneDataObject      * @param typeName FQ data type class name.      */
specifier|public
name|void
name|addType
parameter_list|(
name|String
name|typeName
parameter_list|)
block|{
if|if
condition|(
operator|!
name|canRegisterType
argument_list|(
name|typeName
argument_list|)
condition|)
return|return;
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|typePackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
name|String
name|typeClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
name|typePackageName
operator|.
name|equals
argument_list|(
name|packageName
argument_list|)
condition|)
return|return;
name|importTypesMap
operator|.
name|put
argument_list|(
name|typeClassName
argument_list|,
name|typeName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Add the package name to use for this importUtil invocation.      * @param packageName      */
specifier|public
name|void
name|setPackage
parameter_list|(
name|String
name|packageName
parameter_list|)
block|{
name|this
operator|.
name|packageName
operator|=
name|packageName
expr_stmt|;
block|}
comment|/**      * Performs processing similar to<code>formatJavaType(String)</code>, with special      * handling of primitive types and their Java class counterparts. This method allows      * users to make a decision whether to use primitives or not, regardless of how type      * is mapped.      */
specifier|public
name|String
name|formatJavaType
parameter_list|(
name|String
name|typeName
parameter_list|,
name|boolean
name|usePrimitives
parameter_list|)
block|{
if|if
condition|(
name|usePrimitives
condition|)
block|{
name|String
name|primitive
init|=
operator|(
name|String
operator|)
name|primitivesForClasses
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
return|return
operator|(
name|primitive
operator|!=
literal|null
operator|)
condition|?
name|primitive
else|:
name|formatJavaType
argument_list|(
name|typeName
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|primitiveClass
init|=
operator|(
name|String
operator|)
name|classesForPrimitives
operator|.
name|get
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
return|return
operator|(
name|primitiveClass
operator|!=
literal|null
operator|)
condition|?
name|formatJavaType
argument_list|(
name|primitiveClass
argument_list|)
else|:
name|formatJavaType
argument_list|(
name|typeName
argument_list|)
return|;
block|}
block|}
comment|/**      * Removes registered package and non-reserved registered type name prefixes from java types       */
specifier|public
name|String
name|formatJavaType
parameter_list|(
name|String
name|typeName
parameter_list|)
block|{
if|if
condition|(
name|typeName
operator|!=
literal|null
condition|)
block|{
name|StringUtils
name|stringUtils
init|=
name|StringUtils
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|typeClassName
init|=
name|stringUtils
operator|.
name|stripPackageName
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|reservedImportTypesMap
operator|.
name|containsKey
argument_list|(
name|typeClassName
argument_list|)
condition|)
block|{
if|if
condition|(
name|importTypesMap
operator|.
name|containsKey
argument_list|(
name|typeClassName
argument_list|)
condition|)
block|{
if|if
condition|(
name|typeName
operator|.
name|equals
argument_list|(
name|importTypesMap
operator|.
name|get
argument_list|(
name|typeClassName
argument_list|)
argument_list|)
condition|)
return|return
name|typeClassName
return|;
block|}
block|}
name|String
name|typePackageName
init|=
name|stringUtils
operator|.
name|stripClass
argument_list|(
name|typeName
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"java.lang"
operator|.
name|equals
argument_list|(
name|typePackageName
argument_list|)
condition|)
return|return
name|typeClassName
return|;
if|if
condition|(
operator|(
literal|null
operator|!=
name|packageName
operator|)
operator|&&
operator|(
name|packageName
operator|.
name|equals
argument_list|(
name|typePackageName
argument_list|)
operator|)
condition|)
return|return
name|typeClassName
return|;
block|}
return|return
name|typeName
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|String
name|formatJavaTypeAsNonBooleanPrimitive
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|String
name|value
init|=
operator|(
name|String
operator|)
name|ImportUtils
operator|.
name|classesForPrimitives
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
return|return
name|formatJavaType
argument_list|(
name|value
operator|!=
literal|null
condition|?
name|value
else|:
name|type
argument_list|)
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|isNonBooleanPrimitive
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
name|ImportUtils
operator|.
name|classesForPrimitives
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
operator|&&
operator|!
name|isBoolean
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * @since 3.0      */
specifier|public
name|boolean
name|isBoolean
parameter_list|(
name|String
name|type
parameter_list|)
block|{
return|return
literal|"boolean"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
return|;
block|}
comment|/**      * Generate package and list of import statements based on the registered types.      */
specifier|public
name|String
name|generate
parameter_list|()
block|{
name|StringBuffer
name|outputBuffer
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|packageName
condition|)
block|{
name|outputBuffer
operator|.
name|append
argument_list|(
literal|"package "
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
name|packageName
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|List
name|typesList
init|=
operator|new
name|ArrayList
argument_list|(
name|importTypesMap
operator|.
name|values
argument_list|()
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|typesList
argument_list|,
operator|new
name|Comparator
argument_list|()
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|o1
parameter_list|,
name|Object
name|o2
parameter_list|)
block|{
name|String
name|s1
init|=
operator|(
name|String
operator|)
name|o1
decl_stmt|;
name|String
name|s2
init|=
operator|(
name|String
operator|)
name|o2
decl_stmt|;
for|for
control|(
name|int
name|index
init|=
literal|0
init|;
name|index
operator|<
name|importOrdering
operator|.
name|length
condition|;
name|index
operator|++
control|)
block|{
name|String
name|ordering
init|=
name|importOrdering
index|[
name|index
index|]
decl_stmt|;
if|if
condition|(
operator|(
name|s1
operator|.
name|startsWith
argument_list|(
name|ordering
argument_list|)
operator|)
operator|&&
operator|(
operator|!
name|s2
operator|.
name|startsWith
argument_list|(
name|ordering
argument_list|)
operator|)
condition|)
return|return
operator|-
literal|1
return|;
if|if
condition|(
operator|(
operator|!
name|s1
operator|.
name|startsWith
argument_list|(
name|ordering
argument_list|)
operator|)
operator|&&
operator|(
name|s2
operator|.
name|startsWith
argument_list|(
name|ordering
argument_list|)
operator|)
condition|)
return|return
literal|1
return|;
block|}
return|return
name|s1
operator|.
name|compareTo
argument_list|(
name|s2
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
name|String
name|lastStringPrefix
init|=
literal|null
decl_stmt|;
name|Iterator
name|typesIterator
init|=
name|typesList
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|typesIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|typeName
init|=
operator|(
name|String
operator|)
name|typesIterator
operator|.
name|next
argument_list|()
decl_stmt|;
comment|// Output another newline if we're in a different root package.
comment|// Find root package
name|String
name|thisStringPrefix
init|=
name|typeName
decl_stmt|;
name|int
name|dotIndex
init|=
name|typeName
operator|.
name|indexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
operator|-
literal|1
operator|!=
name|dotIndex
condition|)
block|{
name|thisStringPrefix
operator|=
name|typeName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|dotIndex
argument_list|)
expr_stmt|;
block|}
comment|// if this isn't the first import,
if|if
condition|(
literal|null
operator|!=
name|lastStringPrefix
condition|)
block|{
comment|// and it's different from the last import
if|if
condition|(
literal|false
operator|==
name|thisStringPrefix
operator|.
name|equals
argument_list|(
name|lastStringPrefix
argument_list|)
condition|)
block|{
comment|// output a newline
name|outputBuffer
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
name|lastStringPrefix
operator|=
name|thisStringPrefix
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
literal|"import "
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
name|typeName
argument_list|)
expr_stmt|;
name|outputBuffer
operator|.
name|append
argument_list|(
literal|';'
argument_list|)
expr_stmt|;
if|if
condition|(
name|typesIterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|outputBuffer
operator|.
name|append
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"line.separator"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|outputBuffer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

