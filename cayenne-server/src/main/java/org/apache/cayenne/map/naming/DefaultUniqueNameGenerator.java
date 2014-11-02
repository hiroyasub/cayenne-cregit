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
operator|.
name|naming
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
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultUniqueNameGenerator
implements|implements
name|UniqueNameGenerator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PATTERN
init|=
literal|"%s%d"
decl_stmt|;
specifier|private
specifier|final
name|NameChecker
name|nameChecker
decl_stmt|;
specifier|private
specifier|final
name|String
name|pattern
decl_stmt|;
specifier|public
specifier|static
name|String
name|generate
parameter_list|(
name|NameChecker
name|checker
parameter_list|)
block|{
return|return
name|generate
argument_list|(
name|checker
argument_list|,
name|DEFAULT_PATTERN
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|generate
parameter_list|(
name|NameChecker
name|checker
parameter_list|,
name|Object
name|context
parameter_list|)
block|{
return|return
name|generate
argument_list|(
name|checker
argument_list|,
name|DEFAULT_PATTERN
argument_list|,
name|context
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|generate
parameter_list|(
name|NameChecker
name|checker
parameter_list|,
name|Object
name|context
parameter_list|,
name|String
name|baseName
parameter_list|)
block|{
return|return
name|generate
argument_list|(
name|checker
argument_list|,
name|DEFAULT_PATTERN
argument_list|,
name|context
argument_list|,
name|baseName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|generate
parameter_list|(
name|NameChecker
name|checker
parameter_list|,
name|String
name|pattern
parameter_list|,
name|Object
name|context
parameter_list|,
name|String
name|baseName
parameter_list|)
block|{
name|DefaultUniqueNameGenerator
name|generator
decl_stmt|;
if|if
condition|(
name|checker
operator|==
name|NameCheckers
operator|.
name|embeddable
condition|)
block|{
name|generator
operator|=
operator|new
name|DefaultUniqueNameGenerator
argument_list|(
name|NameCheckers
operator|.
name|embeddable
argument_list|,
name|pattern
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|String
name|generate
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
name|String
name|name
init|=
name|super
operator|.
name|generate
argument_list|(
name|namingContext
argument_list|,
name|nameBase
argument_list|)
decl_stmt|;
name|DataMap
name|map
init|=
operator|(
name|DataMap
operator|)
name|namingContext
decl_stmt|;
if|if
condition|(
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|map
operator|.
name|getDefaultPackage
argument_list|()
operator|+
literal|"."
operator|+
name|name
return|;
block|}
return|return
name|name
return|;
block|}
block|}
expr_stmt|;
block|}
else|else
block|{
name|generator
operator|=
operator|new
name|DefaultUniqueNameGenerator
argument_list|(
name|checker
argument_list|,
name|pattern
argument_list|)
expr_stmt|;
block|}
return|return
name|generator
operator|.
name|generate
argument_list|(
name|context
argument_list|,
name|baseName
argument_list|)
return|;
block|}
specifier|public
name|DefaultUniqueNameGenerator
parameter_list|(
name|NameChecker
name|nameChecker
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|this
operator|.
name|nameChecker
operator|=
name|nameChecker
expr_stmt|;
name|this
operator|.
name|pattern
operator|=
name|pattern
expr_stmt|;
block|}
comment|/**      * Creates a unique name for the new object and constructs this object.      */
specifier|public
name|String
name|generate
parameter_list|(
name|Object
name|namingContext
parameter_list|)
block|{
return|return
name|generate
argument_list|(
name|namingContext
argument_list|,
name|nameChecker
operator|.
name|baseName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @since 1.0.5      */
specifier|public
name|String
name|generate
parameter_list|(
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
return|return
name|generate
argument_list|(
name|pattern
argument_list|,
name|namingContext
argument_list|,
name|nameBase
operator|!=
literal|null
condition|?
name|nameBase
else|:
name|nameChecker
operator|.
name|baseName
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @since 1.0.5      */
specifier|private
name|String
name|generate
parameter_list|(
name|String
name|pattern
parameter_list|,
name|Object
name|namingContext
parameter_list|,
name|String
name|nameBase
parameter_list|)
block|{
name|int
name|c
init|=
literal|1
decl_stmt|;
name|String
name|name
init|=
name|nameBase
decl_stmt|;
while|while
condition|(
name|nameChecker
operator|.
name|isNameInUse
argument_list|(
name|namingContext
argument_list|,
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|String
operator|.
name|format
argument_list|(
name|pattern
argument_list|,
name|nameBase
argument_list|,
name|c
operator|++
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
block|}
end_class

end_unit

