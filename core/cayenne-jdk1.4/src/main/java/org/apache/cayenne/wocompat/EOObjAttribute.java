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
name|wocompat
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
name|ObjAttribute
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
name|ObjEntity
import|;
end_import

begin_comment
comment|/**  * An ObjAttribute extension that accomodates EOModel attributes.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|EOObjAttribute
extends|extends
name|ObjAttribute
block|{
specifier|protected
name|boolean
name|readOnly
decl_stmt|;
specifier|public
name|EOObjAttribute
parameter_list|()
block|{
block|}
specifier|public
name|EOObjAttribute
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EOObjAttribute
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|type
parameter_list|,
name|ObjEntity
name|parent
parameter_list|)
block|{
name|super
argument_list|(
name|name
argument_list|,
name|type
argument_list|,
name|parent
argument_list|)
expr_stmt|;
block|}
comment|/**      * @deprecated since 2.0 use isReadOnly().      */
specifier|public
name|boolean
name|getReadOnly
parameter_list|()
block|{
return|return
name|isReadOnly
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isReadOnly
parameter_list|()
block|{
return|return
name|readOnly
return|;
block|}
specifier|public
name|void
name|setReadOnly
parameter_list|(
name|boolean
name|readOnly
parameter_list|)
block|{
name|this
operator|.
name|readOnly
operator|=
name|readOnly
expr_stmt|;
block|}
block|}
end_class

end_unit

