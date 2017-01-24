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
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_comment
comment|/**  * Handles<code>java.lang.Boolean</code> mapping. Note that "materialize*" methods return  * either Boolean.TRUE or Boolean.FALSE, instead of creating new Boolean instances using  * constructor. This makes possible identity comparison such as  *<code>object.getBooleanProperty() == Boolean.TRUE</code>.  *   * @since 1.2  */
end_comment

begin_class
specifier|public
class|class
name|BooleanType
implements|implements
name|ExtendedType
argument_list|<
name|Boolean
argument_list|>
block|{
annotation|@
name|Override
specifier|public
name|String
name|getClassName
parameter_list|()
block|{
return|return
name|Boolean
operator|.
name|class
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setJdbcObject
parameter_list|(
name|PreparedStatement
name|st
parameter_list|,
name|Boolean
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
name|val
operator|==
literal|null
condition|)
block|{
name|st
operator|.
name|setNull
argument_list|(
name|pos
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|type
operator|==
name|Types
operator|.
name|BIT
operator|||
name|type
operator|==
name|Types
operator|.
name|BOOLEAN
condition|)
block|{
name|boolean
name|flag
init|=
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|val
argument_list|)
decl_stmt|;
name|st
operator|.
name|setBoolean
argument_list|(
name|pos
argument_list|,
name|flag
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
annotation|@
name|Override
specifier|public
name|Boolean
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
name|boolean
name|b
init|=
name|rs
operator|.
name|getBoolean
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
operator|)
condition|?
literal|null
else|:
name|b
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
annotation|@
name|Override
specifier|public
name|Boolean
name|materializeObject
parameter_list|(
name|CallableStatement
name|st
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
name|boolean
name|b
init|=
name|st
operator|.
name|getBoolean
argument_list|(
name|index
argument_list|)
decl_stmt|;
return|return
operator|(
name|st
operator|.
name|wasNull
argument_list|()
operator|)
condition|?
literal|null
else|:
name|b
condition|?
name|Boolean
operator|.
name|TRUE
else|:
name|Boolean
operator|.
name|FALSE
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|(
name|Boolean
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
return|return
literal|'\''
operator|+
name|value
operator|.
name|toString
argument_list|()
operator|+
literal|'\''
return|;
block|}
block|}
end_class

end_unit

