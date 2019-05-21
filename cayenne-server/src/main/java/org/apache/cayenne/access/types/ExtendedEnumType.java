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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|CayenneRuntimeException
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
name|ExtendedEnumeration
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
name|dba
operator|.
name|TypesMapping
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|CallableStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|ResultSet
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
name|Map
import|;
end_import

begin_comment
comment|/**  * An ExtendedType that handles a Java Enum based upon the Cayenne ExtendedEnumeration  * interface. The ExtendedEnumeration interface requires the developer to specify the  * database values for the Enum being mapped. This ExtendedType is used to auto-register  * those Enums found in the model.  *   * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ExtendedEnumType
parameter_list|<
name|T
extends|extends
name|Enum
parameter_list|<
name|T
parameter_list|>
parameter_list|>
implements|implements
name|ExtendedType
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|Class
argument_list|<
name|T
argument_list|>
name|enumerationClass
init|=
literal|null
decl_stmt|;
specifier|private
name|T
index|[]
name|values
init|=
literal|null
decl_stmt|;
comment|// Contains a mapping of database values (Integer or String) and the
comment|// Enum for that value. This is to facilitate mapping database values
comment|// back to the Enum upon reading them from the database.
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Enum
argument_list|<
name|T
argument_list|>
argument_list|>
name|enumerationMappings
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|ExtendedEnumType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumerationClass
parameter_list|)
block|{
if|if
condition|(
name|enumerationClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null ExtendedEnumType class"
argument_list|)
throw|;
block|}
name|this
operator|.
name|enumerationClass
operator|=
name|enumerationClass
expr_stmt|;
try|try
block|{
name|Method
name|m
init|=
name|enumerationClass
operator|.
name|getMethod
argument_list|(
literal|"values"
argument_list|)
decl_stmt|;
name|values
operator|=
operator|(
name|T
index|[]
operator|)
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|T
name|value
range|:
name|values
control|)
block|{
name|register
argument_list|(
name|value
argument_list|,
operator|(
operator|(
name|ExtendedEnumeration
operator|)
name|value
operator|)
operator|.
name|getDatabaseValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Class "
operator|+
name|enumerationClass
operator|.
name|getName
argument_list|()
operator|+
literal|" is not an Enum"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|enumerationClass
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|T
name|materializeObject
parameter_list|(
name|ResultSet
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|int
name|i
init|=
name|rs
operator|.
name|getInt
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|rs
operator|.
name|wasNull
argument_list|()
operator|||
name|index
operator|<
literal|0
operator|)
condition|?
literal|null
else|:
name|lookup
argument_list|(
name|i
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|string
init|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|string
operator|!=
literal|null
condition|?
name|lookup
argument_list|(
name|string
argument_list|)
else|:
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|T
name|materializeObject
parameter_list|(
name|CallableStatement
name|rs
parameter_list|,
name|int
name|index
parameter_list|,
name|int
name|type
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|int
name|i
init|=
name|rs
operator|.
name|getInt
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|rs
operator|.
name|wasNull
argument_list|()
operator|||
name|index
operator|<
literal|0
operator|)
condition|?
literal|null
else|:
name|lookup
argument_list|(
name|i
argument_list|)
return|;
block|}
else|else
block|{
name|String
name|string
init|=
name|rs
operator|.
name|getString
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
name|string
operator|!=
literal|null
condition|?
name|lookup
argument_list|(
name|string
argument_list|)
else|:
literal|null
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|T
name|value
parameter_list|,
name|int
name|pos
parameter_list|,
name|int
name|type
parameter_list|,
name|int
name|precision
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|value
operator|instanceof
name|ExtendedEnumeration
condition|)
block|{
name|ExtendedEnumeration
name|e
init|=
operator|(
name|ExtendedEnumeration
operator|)
name|value
decl_stmt|;
if|if
condition|(
name|TypesMapping
operator|.
name|isNumeric
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|statement
operator|.
name|setInt
argument_list|(
name|pos
argument_list|,
operator|(
name|Integer
operator|)
name|e
operator|.
name|getDatabaseValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|statement
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
operator|(
name|String
operator|)
name|e
operator|.
name|getDatabaseValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|statement
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Register the given enum with the mapped database value.      */
specifier|private
name|void
name|register
parameter_list|(
name|Enum
argument_list|<
name|T
argument_list|>
name|enumeration
parameter_list|,
name|Object
name|databaseValue
parameter_list|)
block|{
comment|// Check for duplicates.
if|if
condition|(
name|enumerationMappings
operator|.
name|containsKey
argument_list|(
name|databaseValue
argument_list|)
operator|||
name|enumerationMappings
operator|.
name|containsValue
argument_list|(
name|enumeration
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Enumerations/values may not be duplicated."
argument_list|)
throw|;
block|}
comment|// Store by database value/enum because we have to lookup by db value later.
name|enumerationMappings
operator|.
name|put
argument_list|(
name|databaseValue
argument_list|,
name|enumeration
argument_list|)
expr_stmt|;
block|}
comment|/**      * Lookup the giving database value and return the matching enum.      */
specifier|private
name|T
name|lookup
parameter_list|(
name|Object
name|databaseValue
parameter_list|)
block|{
if|if
condition|(
operator|!
name|enumerationMappings
operator|.
name|containsKey
argument_list|(
name|databaseValue
argument_list|)
condition|)
block|{
comment|// All integers enums are mapped. Not necessarily all strings.
if|if
condition|(
name|databaseValue
operator|instanceof
name|Integer
condition|)
block|{
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Missing enumeration mapping for %s with value %s."
argument_list|,
name|getClassName
argument_list|()
argument_list|,
name|databaseValue
argument_list|)
throw|;
block|}
comment|// Use the database value (a String) as the enum value.
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumerationClass
argument_list|,
operator|(
name|String
operator|)
name|databaseValue
argument_list|)
return|;
block|}
comment|// Mapped value->enum exists, return it.
return|return
operator|(
name|T
operator|)
name|enumerationMappings
operator|.
name|get
argument_list|(
name|databaseValue
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|T
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return
literal|"NULL"
return|;
block|}
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
comment|// buffer.append(object.getClass().getName()).append(".");
name|buffer
operator|.
name|append
argument_list|(
name|value
operator|.
name|name
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"="
argument_list|)
expr_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|ExtendedEnumeration
condition|)
block|{
name|Object
name|dbValue
init|=
operator|(
operator|(
name|ExtendedEnumeration
operator|)
name|value
operator|)
operator|.
name|getDatabaseValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbValue
operator|instanceof
name|String
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
name|buffer
operator|.
name|append
argument_list|(
name|value
argument_list|)
expr_stmt|;
if|if
condition|(
name|dbValue
operator|instanceof
name|String
condition|)
block|{
name|buffer
operator|.
name|append
argument_list|(
literal|"'"
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|buffer
operator|.
name|append
argument_list|(
name|value
operator|.
name|ordinal
argument_list|()
argument_list|)
expr_stmt|;
comment|// FIXME -- this isn't quite right
block|}
return|return
name|buffer
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns the enumeration mapping for this enumerated data type. The key is the      * database value, the value is the actual enum.      */
specifier|public
name|Map
argument_list|<
name|Object
argument_list|,
name|Enum
argument_list|<
name|T
argument_list|>
argument_list|>
name|getEnumerationMappings
parameter_list|()
block|{
return|return
name|enumerationMappings
return|;
block|}
specifier|public
name|Object
index|[]
name|getValues
parameter_list|()
block|{
return|return
name|values
return|;
block|}
block|}
end_class

end_unit

