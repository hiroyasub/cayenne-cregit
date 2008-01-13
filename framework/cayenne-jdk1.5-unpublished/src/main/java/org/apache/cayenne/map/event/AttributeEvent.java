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
name|map
operator|.
name|event
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

begin_comment
comment|/**   * Represents events resulted from Attribute changes   * in CayenneModeler. This event is used for both ObjAttributes  * and DbAttributes.  *   * @author Misha Shengaout  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|AttributeEvent
extends|extends
name|EntityEvent
block|{
specifier|protected
name|Attribute
name|attribute
decl_stmt|;
comment|/** Creates a Attribute change event. */
specifier|public
name|AttributeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|Attribute
name|attr
parameter_list|,
name|Entity
name|entity
parameter_list|)
block|{
name|super
argument_list|(
name|src
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|setAttribute
argument_list|(
name|attr
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a Attribute event of a specified type. */
specifier|public
name|AttributeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|Attribute
name|attr
parameter_list|,
name|Entity
name|entity
parameter_list|,
name|int
name|id
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
comment|/** Creates a Attribute name change event.*/
specifier|public
name|AttributeEvent
parameter_list|(
name|Object
name|src
parameter_list|,
name|Attribute
name|attr
parameter_list|,
name|Entity
name|entity
parameter_list|,
name|String
name|oldName
parameter_list|)
block|{
name|this
argument_list|(
name|src
argument_list|,
name|attr
argument_list|,
name|entity
argument_list|)
expr_stmt|;
name|setOldName
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
block|}
comment|/** Get attribute (obj or db). */
specifier|public
name|Attribute
name|getAttribute
parameter_list|()
block|{
return|return
name|attribute
return|;
block|}
comment|/**      * Sets the attribute.      * @param attribute The attribute to set      */
specifier|public
name|void
name|setAttribute
parameter_list|(
name|Attribute
name|attribute
parameter_list|)
block|{
name|this
operator|.
name|attribute
operator|=
name|attribute
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNewName
parameter_list|()
block|{
return|return
operator|(
name|attribute
operator|!=
literal|null
operator|)
condition|?
name|attribute
operator|.
name|getName
argument_list|()
else|:
literal|null
return|;
block|}
block|}
end_class

end_unit

