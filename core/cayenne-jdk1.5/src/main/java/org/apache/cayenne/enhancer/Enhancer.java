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

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassVisitor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|objectweb
operator|.
name|asm
operator|.
name|ClassWriter
import|;
end_import

begin_comment
comment|/**  * A ClassFileTransformer that delegates class enhancement to a chain of ASM transformers  * provided by the {@link EnhancerVisitorFactory}.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
specifier|public
class|class
name|Enhancer
implements|implements
name|ClassFileTransformer
block|{
specifier|protected
name|Log
name|logger
decl_stmt|;
specifier|protected
name|EnhancerVisitorFactory
name|visitorFactory
decl_stmt|;
specifier|public
name|Enhancer
parameter_list|(
name|EnhancerVisitorFactory
name|visitorFactory
parameter_list|)
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
name|this
operator|.
name|visitorFactory
operator|=
name|visitorFactory
expr_stmt|;
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
name|ClassReader
name|reader
init|=
operator|new
name|ClassReader
argument_list|(
name|classfileBuffer
argument_list|)
decl_stmt|;
name|ClassWriter
name|writer
init|=
operator|new
name|ClassWriter
argument_list|(
name|reader
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ClassVisitor
name|visitor
init|=
name|visitorFactory
operator|.
name|createVisitor
argument_list|(
name|className
argument_list|,
name|writer
argument_list|)
decl_stmt|;
if|if
condition|(
name|visitor
operator|==
literal|null
condition|)
block|{
comment|// per instrumentation docs, if no transformation occured, we must return null
return|return
literal|null
return|;
block|}
name|logger
operator|.
name|info
argument_list|(
literal|"enhancing class "
operator|+
name|className
argument_list|)
expr_stmt|;
name|reader
operator|.
name|accept
argument_list|(
name|visitor
argument_list|,
literal|true
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
end_class

end_unit

