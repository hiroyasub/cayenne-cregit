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
name|modeler
operator|.
name|graph
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|Attribute
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
name|Entity
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
name|Relationship
import|;
end_import

begin_comment
comment|/**  * Abstract class to describe entity's cell   */
end_comment

begin_class
specifier|abstract
class|class
name|EntityCellMetadata
parameter_list|<
name|E
extends|extends
name|Entity
parameter_list|<
name|E
parameter_list|,
name|A
parameter_list|,
name|R
parameter_list|>
parameter_list|,
name|A
extends|extends
name|Attribute
parameter_list|<
name|E
parameter_list|,
name|A
parameter_list|,
name|R
parameter_list|>
parameter_list|,
name|R
extends|extends
name|Relationship
parameter_list|<
name|E
parameter_list|,
name|A
parameter_list|,
name|R
parameter_list|>
parameter_list|>
implements|implements
name|Serializable
block|{
specifier|final
name|GraphBuilder
argument_list|<
name|E
argument_list|,
name|A
argument_list|,
name|R
argument_list|>
name|builder
decl_stmt|;
specifier|final
name|String
name|entityName
decl_stmt|;
specifier|final
name|String
name|label
decl_stmt|;
name|EntityCellMetadata
parameter_list|(
name|GraphBuilder
argument_list|<
name|E
argument_list|,
name|A
argument_list|,
name|R
argument_list|>
name|builder
parameter_list|,
name|Entity
argument_list|<
name|E
argument_list|,
name|A
argument_list|,
name|R
argument_list|>
name|entity
parameter_list|)
block|{
name|this
operator|.
name|builder
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|builder
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|entity
argument_list|)
operator|.
name|getName
argument_list|()
expr_stmt|;
name|this
operator|.
name|label
operator|=
name|createLabel
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
comment|/**      * Resolves entity      */
specifier|public
specifier|abstract
name|Entity
argument_list|<
name|E
argument_list|,
name|A
argument_list|,
name|R
argument_list|>
name|fetchEntity
parameter_list|()
function_decl|;
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|label
return|;
block|}
comment|/**      * Creates label for this cell      */
name|String
name|createLabel
parameter_list|(
name|Entity
argument_list|<
name|E
argument_list|,
name|A
argument_list|,
name|R
argument_list|>
name|entity
parameter_list|)
block|{
name|StringBuilder
name|label
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"<html><center><u><b>"
argument_list|)
operator|.
name|append
argument_list|(
name|entity
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</b></u></center>"
argument_list|)
decl_stmt|;
for|for
control|(
name|A
name|attr
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
name|isPrimary
argument_list|(
name|attr
argument_list|)
condition|)
block|{
name|label
operator|.
name|append
argument_list|(
literal|"<br><i>"
argument_list|)
operator|.
name|append
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|"</i>"
argument_list|)
expr_stmt|;
block|}
block|}
for|for
control|(
name|A
name|attr
range|:
name|entity
operator|.
name|getAttributes
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|isPrimary
argument_list|(
name|attr
argument_list|)
condition|)
block|{
name|label
operator|.
name|append
argument_list|(
literal|"<br>"
argument_list|)
operator|.
name|append
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|label
operator|.
name|append
argument_list|(
literal|"</html>"
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
comment|/**      * Returns whether attribute is "primary" and should therefore be written in italic      */
specifier|protected
specifier|abstract
name|boolean
name|isPrimary
parameter_list|(
name|A
name|attr
parameter_list|)
function_decl|;
block|}
end_class

end_unit

