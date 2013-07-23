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
name|reflect
package|;
end_package

begin_class
specifier|public
class|class
name|TestJavaBean
extends|extends
name|Object
block|{
specifier|protected
name|String
name|stringField
decl_stmt|;
specifier|protected
name|int
name|intField
decl_stmt|;
specifier|protected
name|boolean
name|booleanField
decl_stmt|;
specifier|protected
name|Integer
name|integerField
decl_stmt|;
specifier|protected
name|Number
name|numberField
decl_stmt|;
specifier|protected
name|byte
index|[]
name|byteArrayField
decl_stmt|;
specifier|protected
name|Object
name|objectField
decl_stmt|;
specifier|protected
name|TestJavaBean
name|related
decl_stmt|;
specifier|public
name|byte
index|[]
name|getByteArrayField
parameter_list|()
block|{
return|return
name|byteArrayField
return|;
block|}
specifier|public
name|void
name|setByteArrayField
parameter_list|(
name|byte
index|[]
name|byteArrayField
parameter_list|)
block|{
name|this
operator|.
name|byteArrayField
operator|=
name|byteArrayField
expr_stmt|;
block|}
specifier|public
name|int
name|getIntField
parameter_list|()
block|{
return|return
name|intField
return|;
block|}
specifier|public
name|void
name|setIntField
parameter_list|(
name|int
name|intField
parameter_list|)
block|{
name|this
operator|.
name|intField
operator|=
name|intField
expr_stmt|;
block|}
specifier|public
name|String
name|getStringField
parameter_list|()
block|{
return|return
name|stringField
return|;
block|}
specifier|public
name|void
name|setStringField
parameter_list|(
name|String
name|stringField
parameter_list|)
block|{
name|this
operator|.
name|stringField
operator|=
name|stringField
expr_stmt|;
block|}
specifier|public
name|Integer
name|getIntegerField
parameter_list|()
block|{
return|return
name|integerField
return|;
block|}
specifier|public
name|void
name|setIntegerField
parameter_list|(
name|Integer
name|integerField
parameter_list|)
block|{
name|this
operator|.
name|integerField
operator|=
name|integerField
expr_stmt|;
block|}
specifier|public
name|Number
name|getNumberField
parameter_list|()
block|{
return|return
name|numberField
return|;
block|}
specifier|public
name|void
name|setNumberField
parameter_list|(
name|Number
name|numberField
parameter_list|)
block|{
name|this
operator|.
name|numberField
operator|=
name|numberField
expr_stmt|;
block|}
specifier|public
name|Object
name|getObjectField
parameter_list|()
block|{
return|return
name|objectField
return|;
block|}
specifier|public
name|void
name|setObjectField
parameter_list|(
name|Object
name|objectField
parameter_list|)
block|{
name|this
operator|.
name|objectField
operator|=
name|objectField
expr_stmt|;
block|}
specifier|public
name|boolean
name|isBooleanField
parameter_list|()
block|{
return|return
name|booleanField
return|;
block|}
specifier|public
name|void
name|setBooleanField
parameter_list|(
name|boolean
name|booleanField
parameter_list|)
block|{
name|this
operator|.
name|booleanField
operator|=
name|booleanField
expr_stmt|;
block|}
specifier|public
name|TestJavaBean
name|getRelated
parameter_list|()
block|{
return|return
name|related
return|;
block|}
specifier|public
name|void
name|setRelated
parameter_list|(
name|TestJavaBean
name|related
parameter_list|)
block|{
name|this
operator|.
name|related
operator|=
name|related
expr_stmt|;
block|}
block|}
end_class

end_unit
