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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|instrument
operator|.
name|IllegalClassFormatException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|ProtectionDomain
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|persistence
operator|.
name|spi
operator|.
name|ClassTransformer
import|;
end_import

begin_comment
comment|/**  * A noop ClassTransformer that logs all classes that were passed through it.  *   */
end_comment

begin_class
specifier|public
class|class
name|MockClassTransformer
implements|implements
name|ClassTransformer
block|{
specifier|protected
name|Collection
argument_list|<
name|String
argument_list|>
name|transformed
decl_stmt|;
specifier|public
name|MockClassTransformer
parameter_list|()
block|{
name|this
operator|.
name|transformed
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getTransformed
parameter_list|()
block|{
return|return
name|transformed
return|;
block|}
specifier|public
name|byte
index|[]
name|transform
parameter_list|(
name|ClassLoader
name|loader
parameter_list|,
name|String
name|className
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|classBeingRedefined
parameter_list|,
name|ProtectionDomain
name|protectionDomain
parameter_list|,
name|byte
index|[]
name|classfileBuffer
parameter_list|)
throws|throws
name|IllegalClassFormatException
block|{
name|transformed
operator|.
name|add
argument_list|(
name|className
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

