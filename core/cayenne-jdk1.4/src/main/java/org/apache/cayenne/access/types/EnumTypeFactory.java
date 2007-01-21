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
name|access
operator|.
name|types
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Constructor
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
name|util
operator|.
name|Util
import|;
end_import

begin_comment
comment|/**  * ExtendedTypeFactory for handling JDK 1.5 Enums. Gracefully handles JDK 1.4 environment.  *   * @since 3.0  * @author Andrus Adamchik  */
end_comment

begin_class
class|class
name|EnumTypeFactory
implements|implements
name|ExtendedTypeFactory
block|{
specifier|private
name|Constructor
name|enumTypeConstructor
decl_stmt|;
name|EnumTypeFactory
parameter_list|()
block|{
comment|// see if we can support enums
try|try
block|{
name|Class
name|enumTypeClass
init|=
name|Util
operator|.
name|getJavaClass
argument_list|(
literal|"org.apache.cayenne.access.types.EnumType"
argument_list|)
decl_stmt|;
name|enumTypeConstructor
operator|=
name|enumTypeClass
operator|.
name|getConstructor
argument_list|(
operator|new
name|Class
index|[]
block|{
name|Class
operator|.
name|class
block|}
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// no enums support... either Java 1.4 or Cayenne 1.5 extensions are absent
block|}
block|}
specifier|public
name|ExtendedType
name|getType
parameter_list|(
name|Class
name|objectClass
parameter_list|)
block|{
if|if
condition|(
name|enumTypeConstructor
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
comment|// load EnumType via reflection as the source has to stay JDK 1.4 compliant
name|ExtendedType
name|type
init|=
operator|(
name|ExtendedType
operator|)
name|enumTypeConstructor
operator|.
name|newInstance
argument_list|(
operator|new
name|Object
index|[]
block|{
name|objectClass
block|}
argument_list|)
decl_stmt|;
return|return
name|type
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// ignore exceptions...
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

