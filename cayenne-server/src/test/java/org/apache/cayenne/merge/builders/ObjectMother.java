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
name|merge
operator|.
name|builders
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
name|DataMap
import|;
end_import

begin_comment
comment|/**  * Factory for test data see pattern definition:  * http://martinfowler.com/bliki/ObjectMother.html  *  * @since 4.0.  */
end_comment

begin_class
specifier|public
class|class
name|ObjectMother
block|{
specifier|public
specifier|static
name|DataMapBuilder
name|dataMap
parameter_list|()
block|{
return|return
operator|new
name|DataMapBuilder
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|DataMapBuilder
name|dataMap
parameter_list|(
name|DataMap
name|dataMap
parameter_list|)
block|{
return|return
operator|new
name|DataMapBuilder
argument_list|(
name|dataMap
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DbEntityBuilder
name|dbEntity
parameter_list|()
block|{
return|return
operator|new
name|DbEntityBuilder
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|DbEntityBuilder
name|dbEntity
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|DbEntityBuilder
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ObjEntityBuilder
name|objEntity
parameter_list|()
block|{
return|return
operator|new
name|ObjEntityBuilder
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|ObjEntityBuilder
name|objEntity
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|className
parameter_list|,
name|String
name|table
parameter_list|)
block|{
return|return
operator|new
name|ObjEntityBuilder
argument_list|()
operator|.
name|name
argument_list|(
name|className
argument_list|)
operator|.
name|clazz
argument_list|(
name|packageName
operator|+
literal|"."
operator|+
name|className
argument_list|)
operator|.
name|dbEntity
argument_list|(
name|table
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ObjAttributeBuilder
name|objAttr
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|ObjAttributeBuilder
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DbAttributeBuilder
name|dbAttr
parameter_list|(
name|String
name|name
parameter_list|)
block|{
return|return
name|dbAttr
argument_list|()
operator|.
name|name
argument_list|(
name|name
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|DbAttributeBuilder
name|dbAttr
parameter_list|()
block|{
return|return
operator|new
name|DbAttributeBuilder
argument_list|()
return|;
block|}
block|}
end_class

end_unit

