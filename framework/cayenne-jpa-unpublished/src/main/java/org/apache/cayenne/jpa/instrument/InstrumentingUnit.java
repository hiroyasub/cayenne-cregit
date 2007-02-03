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
name|instrument
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
name|ClassFileTransformer
import|;
end_import

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
name|javax
operator|.
name|persistence
operator|.
name|spi
operator|.
name|ClassTransformer
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
name|instrument
operator|.
name|InstrumentUtil
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
name|jpa
operator|.
name|JpaUnit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|Log
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|logging
operator|.
name|LogFactory
import|;
end_import

begin_comment
comment|/**  * A unit that loads all transformers into a shared Instrumentation instance.  *   * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|InstrumentingUnit
extends|extends
name|JpaUnit
block|{
specifier|protected
name|Log
name|logger
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|addTransformer
parameter_list|(
specifier|final
name|ClassTransformer
name|transformer
parameter_list|)
block|{
comment|// sanity check
if|if
condition|(
operator|!
name|InstrumentUtil
operator|.
name|isAgentLoaded
argument_list|()
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"*** No instrumentation instance present. "
operator|+
literal|"Check the -javaagent: option"
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// wrap in a ClassFileTransformer
name|ClassFileTransformer
name|transformerWrapper
init|=
operator|new
name|ClassFileTransformer
argument_list|()
block|{
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
return|return
name|transformer
operator|.
name|transform
argument_list|(
name|loader
argument_list|,
name|className
argument_list|,
name|classBeingRedefined
argument_list|,
name|protectionDomain
argument_list|,
name|classfileBuffer
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"*** Adding transformer: "
operator|+
name|transformer
argument_list|)
expr_stmt|;
name|InstrumentUtil
operator|.
name|addTransformer
argument_list|(
name|transformerWrapper
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|Log
name|getLogger
parameter_list|()
block|{
if|if
condition|(
name|logger
operator|==
literal|null
condition|)
block|{
name|logger
operator|=
name|LogFactory
operator|.
name|getLog
argument_list|(
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|logger
return|;
block|}
block|}
end_class

end_unit

