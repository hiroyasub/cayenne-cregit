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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|builder
operator|.
name|ToStringBuilder
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
name|BeanValidationFailure
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
comment|/**  * A convenience superclass of ExtendedType implementations. Implements  * {@link #setJdbcObject(PreparedStatement, Object, int, int, int)}in a generic fashion  * by calling "setObject(..)" on PreparedStatement. Some adapters may need to override  * this behavior as it doesn't work consistently across all JDBC drivers.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractType
implements|implements
name|ExtendedType
block|{
comment|/**      * Helper method for ExtendedType implementors to check for null required values.      *       * @since 1.2      * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
specifier|public
specifier|static
name|boolean
name|validateNull
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
if|if
condition|(
name|dbAttribute
operator|.
name|isMandatory
argument_list|()
operator|&&
name|value
operator|==
literal|null
condition|)
block|{
name|validationResult
operator|.
name|addFailure
argument_list|(
operator|new
name|BeanValidationFailure
argument_list|(
name|source
argument_list|,
name|property
argument_list|,
literal|"'"
operator|+
name|property
operator|+
literal|"' must be not null"
argument_list|)
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
comment|/**      * Calls "PreparedStatement.setObject(..)". Some DbAdapters may need to override this      * behavior for at least some of the object types, as it doesn't work consistently      * across all JDBC drivers.      */
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Object
name|val
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
name|precision
operator|!=
operator|-
literal|1
condition|)
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|,
name|precision
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|st
operator|.
name|setObject
argument_list|(
name|pos
argument_list|,
name|val
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|abstract
name|String
name|getClassName
parameter_list|()
function_decl|;
specifier|public
specifier|abstract
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
function_decl|;
specifier|public
specifier|abstract
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
function_decl|;
comment|/**      * Always returns true. Simplifies subclass implementation, as only some of the types      * can perform the validation.      *       * @deprecated since 3.0 as validation should not be done at the DataNode level.      */
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
literal|true
return|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
operator|new
name|ToStringBuilder
argument_list|(
name|this
argument_list|)
operator|.
name|append
argument_list|(
literal|"className"
argument_list|,
name|getClassName
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

