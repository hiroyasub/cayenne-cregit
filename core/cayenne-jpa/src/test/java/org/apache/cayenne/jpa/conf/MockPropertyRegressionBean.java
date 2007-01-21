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
name|jpa
operator|.
name|conf
package|;
end_package

begin_class
specifier|public
class|class
name|MockPropertyRegressionBean
block|{
specifier|public
name|MockPropertyRegressionBean
parameter_list|()
block|{
comment|// avoid eclipse warnings due to unused provate members
name|getP1
argument_list|()
expr_stmt|;
name|setP1
argument_list|(
literal|"x"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getP1
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|private
name|void
name|setP1
parameter_list|(
name|String
name|value
parameter_list|)
block|{
block|}
specifier|protected
name|String
name|getP2
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|protected
name|void
name|setP2
parameter_list|(
name|String
name|value
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getP3
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setP3
parameter_list|(
name|String
name|value
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getP4
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|setP4
parameter_list|(
name|String
name|value
parameter_list|,
name|Object
name|anotherValue
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getP5
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Object
name|setP5
parameter_list|(
name|String
name|value
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

