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
name|access
operator|.
name|types
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|map
operator|.
name|DbAttribute
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
name|validation
operator|.
name|ValidationResult
import|;
end_import

begin_comment
comment|/**  * An ExtendedType that handles an enum class. If Enum is an instance of  * ExtendedEnumeration (preferred), use the mapped database value (provided  * by the getDatabaseValue() method) to map enumerations to the database.  If  * the enum is not an instance of ExtendedEnumeration, fall back to a more  * simplistic mapping.  If mapped to a character column, the name is used as  * the persistent value; if it is mapped to a numeric column, the ordinal  * value (i.e. a position in enum class) is used.  *<p>  *<i>Requires Java 1.5 or newer</i>  *</p>  *   * @since 1.2  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EnumType
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
block|{
specifier|protected
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
decl_stmt|;
comment|//    protected Object[] values;
comment|// Contains a mapping of database values (Integer or String) and the Enum for that value.
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
argument_list|<
name|Object
argument_list|,
name|Enum
argument_list|<
name|T
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|EnumType
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|enumClass
parameter_list|)
block|{
if|if
condition|(
name|enumClass
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Null enum class"
argument_list|)
throw|;
block|}
name|this
operator|.
name|enumClass
operator|=
name|enumClass
expr_stmt|;
try|try
block|{
name|Method
name|m
init|=
name|enumClass
operator|.
name|getMethod
argument_list|(
literal|"values"
argument_list|)
decl_stmt|;
name|Object
index|[]
name|values
init|=
operator|(
name|Object
index|[]
operator|)
name|m
operator|.
name|invoke
argument_list|(
literal|null
argument_list|)
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
name|values
operator|.
name|length
condition|;
name|i
operator|++
control|)
if|if
condition|(
name|values
index|[
name|i
index|]
operator|instanceof
name|ExtendedEnumeration
condition|)
name|register
argument_list|(
operator|(
name|Enum
argument_list|<
name|T
argument_list|>
operator|)
name|values
index|[
name|i
index|]
argument_list|,
operator|(
operator|(
name|ExtendedEnumeration
operator|)
name|values
index|[
name|i
index|]
operator|)
operator|.
name|getDatabaseValue
argument_list|()
argument_list|)
expr_stmt|;
else|else
name|register
argument_list|(
operator|(
name|Enum
argument_list|<
name|T
argument_list|>
operator|)
name|values
index|[
name|i
index|]
argument_list|,
name|i
argument_list|)
expr_stmt|;
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
name|enumClass
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
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|enumClass
operator|.
name|getName
argument_list|()
return|;
block|}
comment|/**      * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
specifier|public
name|boolean
name|validateProperty
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|property
parameter_list|,
name|Object
name|value
parameter_list|,
name|DbAttribute
name|dbAttribute
parameter_list|,
name|ValidationResult
name|validationResult
parameter_list|)
block|{
return|return
name|AbstractType
operator|.
name|validateNull
argument_list|(
name|source
argument_list|,
name|property
argument_list|,
name|value
argument_list|,
name|dbAttribute
argument_list|,
name|validationResult
argument_list|)
return|;
block|}
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|statement
parameter_list|,
name|Object
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
name|Enum
condition|)
block|{
name|Enum
argument_list|<
name|?
argument_list|>
name|e
init|=
operator|(
name|Enum
argument_list|<
name|?
argument_list|>
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
if|if
condition|(
name|e
operator|instanceof
name|ExtendedEnumeration
condition|)
name|statement
operator|.
name|setInt
argument_list|(
name|pos
argument_list|,
operator|(
name|Integer
operator|)
operator|(
operator|(
name|ExtendedEnumeration
operator|)
name|e
operator|)
operator|.
name|getDatabaseValue
argument_list|()
argument_list|)
expr_stmt|;
else|else
name|statement
operator|.
name|setInt
argument_list|(
name|pos
argument_list|,
name|e
operator|.
name|ordinal
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|e
operator|instanceof
name|ExtendedEnumeration
condition|)
name|statement
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
operator|(
name|String
operator|)
operator|(
operator|(
name|ExtendedEnumeration
operator|)
name|e
operator|)
operator|.
name|getDatabaseValue
argument_list|()
argument_list|)
expr_stmt|;
else|else
name|statement
operator|.
name|setString
argument_list|(
name|pos
argument_list|,
name|e
operator|.
name|name
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
specifier|public
name|Object
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
comment|//            return (rs.wasNull() || index< 0) ? null : values[i];
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
comment|//            return string != null ? Enum.valueOf(enumClass, string) : null;
block|}
block|}
specifier|public
name|Object
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
comment|//            return (rs.wasNull() || index< 0) ? null : values[i];
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
comment|//            return string != null ? Enum.valueOf(enumClass, string) : null;
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
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Enumerations/values may not be duplicated."
argument_list|)
throw|;
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
name|Enum
argument_list|<
name|T
argument_list|>
name|lookup
parameter_list|(
name|Object
name|databaseValue
parameter_list|)
block|{
if|if
condition|(
name|enumerationMappings
operator|.
name|containsKey
argument_list|(
name|databaseValue
argument_list|)
operator|==
literal|false
condition|)
block|{
comment|// All integers enums are mapped.  Not necessarily all strings.
if|if
condition|(
name|databaseValue
operator|instanceof
name|Integer
condition|)
throw|throw
operator|new
name|CayenneRuntimeException
argument_list|(
literal|"Missing enumeration mapping for "
operator|+
name|getClassName
argument_list|()
operator|+
literal|" with value "
operator|+
name|databaseValue
operator|+
literal|"."
argument_list|)
throw|;
comment|// Use the database value (a String) as the enum value.
return|return
name|Enum
operator|.
name|valueOf
argument_list|(
name|enumClass
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
name|enumerationMappings
operator|.
name|get
argument_list|(
name|databaseValue
argument_list|)
return|;
block|}
comment|/**      * Returns the enumeration mapping for this enumerated data type.  The      * key is the database value, the value is the actual enum.      */
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
comment|/**      * Returns the values registered for this enumerated data type.  Note that      * the order the values are returned in could differ from the ordinal order      * in which they are declared in the actual enum class.      */
specifier|public
name|Collection
argument_list|<
name|Enum
argument_list|<
name|T
argument_list|>
argument_list|>
name|values
parameter_list|()
block|{
return|return
name|enumerationMappings
operator|.
name|values
argument_list|()
return|;
block|}
block|}
end_class

end_unit

