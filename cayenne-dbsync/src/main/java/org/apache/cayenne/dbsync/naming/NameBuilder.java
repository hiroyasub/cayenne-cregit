begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      https://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|dbsync
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
name|configuration
operator|.
name|ConfigurationNode
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
name|map
operator|.
name|ObjEntity
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
import|;
end_import

begin_comment
comment|/**  * A builder of names for model objects. Ensures that newly generated names do not conflict with the names of siblings  * under the same parent node. Name generation can be performed based on default base names for each model object type,  * or with a user-provided base name. Used standalone or in conjunction with {@link ObjectNameGenerator} that implements  * DB-to-object name mapping conversions. Names generated by {@link ObjectNameGenerator} can be used as "base names" for  * {@link NameBuilder}.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|NameBuilder
block|{
specifier|protected
name|ConfigurationNode
name|nodeToName
decl_stmt|;
specifier|protected
name|ConfigurationNode
name|parent
decl_stmt|;
specifier|protected
name|String
name|dupesPattern
decl_stmt|;
specifier|protected
name|String
name|baseName
decl_stmt|;
specifier|protected
name|NameBuilder
parameter_list|(
name|ConfigurationNode
name|nodeToName
parameter_list|)
block|{
name|this
operator|.
name|nodeToName
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|nodeToName
argument_list|)
expr_stmt|;
name|this
operator|.
name|dupesPattern
operator|=
literal|"%s%d"
expr_stmt|;
block|}
specifier|public
specifier|static
name|NameBuilder
name|builder
parameter_list|(
name|ConfigurationNode
name|node
parameter_list|)
block|{
return|return
operator|new
name|NameBuilder
argument_list|(
name|node
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|NameBuilder
name|builder
parameter_list|(
name|ConfigurationNode
name|node
parameter_list|,
name|ConfigurationNode
name|parent
parameter_list|)
block|{
return|return
operator|new
name|NameBuilder
argument_list|(
name|node
argument_list|)
operator|.
name|in
argument_list|(
name|parent
argument_list|)
return|;
block|}
comment|/**      * A special builder starter for callback methods. Eventually callback methods will be made into ConfigurationNodes,      * and we can use regular {@link #builder(ConfigurationNode)} methods to name them.      */
comment|// TODO: fold CallbackMethod to org.apache.cayenne.map package and make it a ConfigurationNode
comment|// then we can use normal API for it... for now have to keep a special one-off method...
specifier|public
specifier|static
name|NameBuilder
name|builderForCallbackMethod
parameter_list|(
name|ObjEntity
name|parent
parameter_list|)
block|{
return|return
operator|new
name|CallbackNameBuilder
argument_list|()
operator|.
name|in
argument_list|(
name|parent
argument_list|)
return|;
block|}
specifier|public
name|NameBuilder
name|in
parameter_list|(
name|ConfigurationNode
name|parent
parameter_list|)
block|{
name|this
operator|.
name|parent
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|parent
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|NameBuilder
name|dupesPattern
parameter_list|(
name|String
name|dupesPattern
parameter_list|)
block|{
name|this
operator|.
name|dupesPattern
operator|=
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|dupesPattern
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|NameBuilder
name|baseName
parameter_list|(
name|String
name|baseName
parameter_list|)
block|{
name|this
operator|.
name|baseName
operator|=
name|baseName
expr_stmt|;
return|return
name|this
return|;
block|}
specifier|public
name|String
name|name
parameter_list|()
block|{
name|String
name|baseName
init|=
name|this
operator|.
name|baseName
operator|!=
literal|null
operator|&&
name|this
operator|.
name|baseName
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|this
operator|.
name|baseName
else|:
name|nodeToName
operator|.
name|acceptVisitor
argument_list|(
name|DefaultBaseNameVisitor
operator|.
name|INSTANCE
argument_list|)
decl_stmt|;
name|String
name|normalizedBaseName
init|=
name|nodeToName
operator|.
name|acceptVisitor
argument_list|(
operator|new
name|NormalizationVisitor
argument_list|(
name|baseName
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|nodeToName
operator|.
name|acceptVisitor
argument_list|(
operator|new
name|DeduplicationVisitor
argument_list|(
name|parent
argument_list|,
name|normalizedBaseName
argument_list|,
name|dupesPattern
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

