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
name|java
operator|.
name|util
operator|.
name|Map
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
name|jpa
operator|.
name|JpaProviderException
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
name|map
operator|.
name|JpaClassDescriptor
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
comment|/**  * A ClassFileTransformer decorator that wraps a Java instrumentation ClassFileTransformer  * instance in a JPA ClassTransformer. UnitClassTranformer would only do transformation of  * the mapped classes.  *   */
end_comment

begin_class
specifier|public
class|class
name|UnitClassTransformer
implements|implements
name|ClassTransformer
block|{
specifier|protected
name|ClassLoader
name|tempClassLoader
decl_stmt|;
specifier|protected
name|Log
name|logger
decl_stmt|;
specifier|protected
name|ClassFileTransformer
name|transformer
decl_stmt|;
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|JpaClassDescriptor
argument_list|>
name|managedClasses
decl_stmt|;
specifier|public
name|UnitClassTransformer
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|JpaClassDescriptor
argument_list|>
name|managedClasses
parameter_list|,
name|ClassLoader
name|tempClassLoader
parameter_list|,
name|ClassFileTransformer
name|transformer
parameter_list|)
block|{
name|this
operator|.
name|transformer
operator|=
name|transformer
expr_stmt|;
name|this
operator|.
name|managedClasses
operator|=
name|managedClasses
expr_stmt|;
name|this
operator|.
name|tempClassLoader
operator|=
name|tempClassLoader
expr_stmt|;
name|this
operator|.
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
if|if
condition|(
name|tempClassLoader
operator|==
name|loader
condition|)
block|{
return|return
literal|null
return|;
block|}
if|if
condition|(
name|isManagedClass
argument_list|(
name|className
argument_list|)
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Will transform managed class: "
operator|+
name|className
argument_list|)
expr_stmt|;
try|try
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
catch|catch
parameter_list|(
name|IllegalClassFormatException
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error transforming class "
operator|+
name|className
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"Error transforming class "
operator|+
name|className
argument_list|,
name|th
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|JpaProviderException
argument_list|(
literal|"Error transforming class "
operator|+
name|className
argument_list|,
name|th
argument_list|)
throw|;
block|}
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
comment|/**      * Returns true if a classname os a part of an entity map. Note that the class name is      * expected in the internal format, separated by "/", not ".".      */
specifier|protected
name|boolean
name|isManagedClass
parameter_list|(
name|String
name|className
parameter_list|)
block|{
return|return
name|managedClasses
operator|.
name|containsKey
argument_list|(
name|className
operator|.
name|replace
argument_list|(
literal|'/'
argument_list|,
literal|'.'
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

