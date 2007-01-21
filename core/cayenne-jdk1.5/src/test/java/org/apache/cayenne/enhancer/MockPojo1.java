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
name|enhancer
package|;
end_package

begin_class
specifier|public
class|class
name|MockPojo1
block|{
specifier|protected
name|String
name|attribute1
decl_stmt|;
specifier|protected
name|int
name|attribute2
decl_stmt|;
specifier|protected
name|double
name|attribute3
decl_stmt|;
specifier|protected
name|short
name|attribute5
decl_stmt|;
specifier|protected
name|char
name|attribute6
decl_stmt|;
specifier|protected
name|byte
name|attribute7
decl_stmt|;
specifier|protected
name|boolean
name|attribute8
decl_stmt|;
specifier|protected
name|long
name|attribute9
decl_stmt|;
specifier|protected
name|float
name|attribute10
decl_stmt|;
specifier|protected
name|byte
index|[]
name|byteArrayAttribute
decl_stmt|;
specifier|public
name|String
name|getAttribute1
parameter_list|()
block|{
return|return
name|attribute1
return|;
block|}
specifier|public
name|void
name|setAttribute1
parameter_list|(
name|String
name|attribute1
parameter_list|)
block|{
name|this
operator|.
name|attribute1
operator|=
name|attribute1
expr_stmt|;
block|}
specifier|public
name|int
name|getAttribute2
parameter_list|()
block|{
return|return
name|attribute2
return|;
block|}
specifier|public
name|void
name|setAttribute2
parameter_list|(
name|int
name|attribute2
parameter_list|)
block|{
name|this
operator|.
name|attribute2
operator|=
name|attribute2
expr_stmt|;
block|}
specifier|public
name|double
name|getAttribute3
parameter_list|()
block|{
return|return
name|attribute3
return|;
block|}
specifier|public
name|void
name|setAttribute3
parameter_list|(
name|double
name|attribute3
parameter_list|)
block|{
name|this
operator|.
name|attribute3
operator|=
name|attribute3
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|getByteArrayAttribute
parameter_list|()
block|{
return|return
name|byteArrayAttribute
return|;
block|}
specifier|public
name|void
name|setByteArrayAttribute
parameter_list|(
name|byte
index|[]
name|attribute4
parameter_list|)
block|{
name|this
operator|.
name|byteArrayAttribute
operator|=
name|attribute4
expr_stmt|;
block|}
specifier|public
name|short
name|getAttribute5
parameter_list|()
block|{
return|return
name|attribute5
return|;
block|}
specifier|public
name|void
name|setAttribute5
parameter_list|(
name|short
name|attribute5
parameter_list|)
block|{
name|this
operator|.
name|attribute5
operator|=
name|attribute5
expr_stmt|;
block|}
specifier|public
name|char
name|getAttribute6
parameter_list|()
block|{
return|return
name|attribute6
return|;
block|}
specifier|public
name|void
name|setAttribute6
parameter_list|(
name|char
name|attribute6
parameter_list|)
block|{
name|this
operator|.
name|attribute6
operator|=
name|attribute6
expr_stmt|;
block|}
specifier|public
name|byte
name|getAttribute7
parameter_list|()
block|{
return|return
name|attribute7
return|;
block|}
specifier|public
name|void
name|setAttribute7
parameter_list|(
name|byte
name|attribute7
parameter_list|)
block|{
name|this
operator|.
name|attribute7
operator|=
name|attribute7
expr_stmt|;
block|}
specifier|public
name|boolean
name|isAttribute8
parameter_list|()
block|{
return|return
name|attribute8
return|;
block|}
specifier|public
name|void
name|setAttribute8
parameter_list|(
name|boolean
name|attribute8
parameter_list|)
block|{
name|this
operator|.
name|attribute8
operator|=
name|attribute8
expr_stmt|;
block|}
specifier|public
name|long
name|getAttribute9
parameter_list|()
block|{
return|return
name|attribute9
return|;
block|}
specifier|public
name|void
name|setAttribute9
parameter_list|(
name|long
name|attribute9
parameter_list|)
block|{
name|this
operator|.
name|attribute9
operator|=
name|attribute9
expr_stmt|;
block|}
specifier|public
name|float
name|getAttribute10
parameter_list|()
block|{
return|return
name|attribute10
return|;
block|}
specifier|public
name|void
name|setAttribute10
parameter_list|(
name|float
name|attribute10
parameter_list|)
block|{
name|this
operator|.
name|attribute10
operator|=
name|attribute10
expr_stmt|;
block|}
block|}
end_class

end_unit

