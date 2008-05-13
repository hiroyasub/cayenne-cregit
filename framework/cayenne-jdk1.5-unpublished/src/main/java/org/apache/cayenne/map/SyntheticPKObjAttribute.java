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
package|;
end_package

begin_comment
comment|/**  * A "synthetic" server-side ObjAttribute used to describe unmapped PK>.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|SyntheticPKObjAttribute
extends|extends
name|ObjAttribute
block|{
name|SyntheticPKObjAttribute
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
annotation|@
name|Override
specifier|public
name|ObjAttribute
name|getClientAttribute
parameter_list|()
block|{
name|ClientObjAttribute
name|attribute
init|=
operator|new
name|ClientObjAttribute
argument_list|(
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|attribute
operator|.
name|setType
argument_list|(
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// unconditionally expose DbAttribute path and configure as mandatory.
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
name|dbAttributePath
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setMandatory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|DbAttribute
name|dbAttribute
init|=
name|getDbAttribute
argument_list|()
decl_stmt|;
if|if
condition|(
name|dbAttribute
operator|!=
literal|null
condition|)
block|{
name|attribute
operator|.
name|setMaxLength
argument_list|(
name|dbAttribute
operator|.
name|getMaxLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: will likely need "userForLocking" property as well.
return|return
name|attribute
return|;
block|}
block|}
end_class

end_unit

