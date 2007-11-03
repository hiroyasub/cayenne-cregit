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
name|modeler
operator|.
name|editor
package|;
end_package

begin_comment
comment|/**  * Entity for callback type.  * Contains type and type name  *  * @author Vasil Tarasevich  * @version 1.0 Oct 26, 2007  */
end_comment

begin_class
specifier|public
class|class
name|CallbackType
block|{
comment|/**      * callback type ID      */
specifier|private
name|int
name|type
decl_stmt|;
comment|/**      * callback type name      */
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      * constructor      * @param type type id      * @param name name      */
specifier|public
name|CallbackType
parameter_list|(
name|int
name|type
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * @return callback type id      */
specifier|public
name|int
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
comment|/**      * @return callback name      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * @return callback name      */
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

