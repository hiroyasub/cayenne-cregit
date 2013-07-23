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
name|configuration
operator|.
name|DataChannelDescriptor
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
name|event
operator|.
name|CayenneEvent
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
comment|/**  * Superclass of CayenneModeler events.  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|MapEvent
extends|extends
name|CayenneEvent
block|{
comment|/**      * A type that describes object modification events. CHANGE is a default type of new      * MapEvents, unless the type is specified explicitly.      */
specifier|public
specifier|static
specifier|final
name|int
name|CHANGE
init|=
literal|1
decl_stmt|;
comment|/**      * A type that describes object creation events.      */
specifier|public
specifier|static
specifier|final
name|int
name|ADD
init|=
literal|2
decl_stmt|;
comment|/**      * A type that describes object removal events.      */
specifier|public
specifier|static
specifier|final
name|int
name|REMOVE
init|=
literal|3
decl_stmt|;
specifier|protected
name|int
name|id
init|=
name|CHANGE
decl_stmt|;
specifier|protected
name|String
name|oldName
decl_stmt|;
specifier|protected
name|boolean
name|oldNameSet
decl_stmt|;
comment|/**      * Domain of event object. Might be null      */
specifier|protected
name|DataChannelDescriptor
name|domain
decl_stmt|;
comment|/**      * Constructor for MapEvent.      *       * @param source event source      */
specifier|public
name|MapEvent
parameter_list|(
name|Object
name|source
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
comment|/**      * Constructor for MapEvent.      *       * @param source event source      */
specifier|public
name|MapEvent
parameter_list|(
name|Object
name|source
parameter_list|,
name|String
name|oldName
parameter_list|)
block|{
name|super
argument_list|(
name|source
argument_list|)
expr_stmt|;
name|setOldName
argument_list|(
name|oldName
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|isNameChange
parameter_list|()
block|{
return|return
name|oldNameSet
operator|&&
operator|!
name|Util
operator|.
name|nullSafeEquals
argument_list|(
name|getOldName
argument_list|()
argument_list|,
name|getNewName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the id.      *       * @return int      */
specifier|public
name|int
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
comment|/**      * Returns the newName of the object that caused this event.      */
specifier|public
specifier|abstract
name|String
name|getNewName
parameter_list|()
function_decl|;
comment|/**      * Returns the oldName.      */
specifier|public
name|String
name|getOldName
parameter_list|()
block|{
return|return
name|oldName
return|;
block|}
comment|/**      * Sets the id.      */
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|/**      * Sets the oldName.      */
specifier|public
name|void
name|setOldName
parameter_list|(
name|String
name|oldName
parameter_list|)
block|{
name|this
operator|.
name|oldName
operator|=
name|oldName
expr_stmt|;
name|this
operator|.
name|oldNameSet
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Sets domain of event object.      */
specifier|public
name|void
name|setDomain
parameter_list|(
name|DataChannelDescriptor
name|domain
parameter_list|)
block|{
name|this
operator|.
name|domain
operator|=
name|domain
expr_stmt|;
block|}
comment|/**      * @return Domain of event object. Might be null      */
specifier|public
name|DataChannelDescriptor
name|getDomain
parameter_list|()
block|{
return|return
name|domain
return|;
block|}
block|}
end_class

end_unit
