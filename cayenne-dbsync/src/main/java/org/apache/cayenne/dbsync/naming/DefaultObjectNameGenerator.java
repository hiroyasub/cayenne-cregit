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
name|map
operator|.
name|DbAttribute
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
name|DbEntity
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
name|DbJoin
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
name|DbRelationship
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

begin_import
import|import
name|org
operator|.
name|jvnet
operator|.
name|inflector
operator|.
name|Noun
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Locale
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
comment|/**  * The default strategy for converting DB-layer to Object-layer names.  *  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|DefaultObjectNameGenerator
implements|implements
name|ObjectNameGenerator
block|{
specifier|private
name|DbEntityNameStemmer
name|dbEntityNameStemmer
decl_stmt|;
specifier|public
name|DefaultObjectNameGenerator
parameter_list|()
block|{
name|this
operator|.
name|dbEntityNameStemmer
operator|=
name|NoStemStemmer
operator|.
name|getInstance
argument_list|()
expr_stmt|;
block|}
specifier|public
name|DefaultObjectNameGenerator
parameter_list|(
name|DbEntityNameStemmer
name|dbEntityNameStemmer
parameter_list|)
block|{
name|this
operator|.
name|dbEntityNameStemmer
operator|=
name|dbEntityNameStemmer
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|relationshipName
parameter_list|(
name|DbRelationship
modifier|...
name|relationshipChain
parameter_list|)
block|{
if|if
condition|(
name|relationshipChain
operator|==
literal|null
operator|||
name|relationshipChain
operator|.
name|length
operator|<
literal|1
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"At least on relationship is expected: "
operator|+
name|relationshipChain
argument_list|)
throw|;
block|}
comment|// ignore the name of DbRelationship itself (FWIW we may be generating a new name for it here)...
comment|// generate the name based on join semantics...
name|String
name|name
init|=
name|isToMany
argument_list|(
name|relationshipChain
argument_list|)
condition|?
name|toManyRelationshipName
argument_list|(
name|relationshipChain
argument_list|)
else|:
name|toOneRelationshipName
argument_list|(
name|relationshipChain
argument_list|)
decl_stmt|;
return|return
name|Util
operator|.
name|underscoredToJava
argument_list|(
name|name
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isToMany
parameter_list|(
name|DbRelationship
modifier|...
name|relationshipChain
parameter_list|)
block|{
for|for
control|(
name|DbRelationship
name|r
range|:
name|relationshipChain
control|)
block|{
if|if
condition|(
name|r
operator|.
name|isToMany
argument_list|()
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|String
name|stemmed
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
return|return
name|dbEntityNameStemmer
operator|.
name|stem
argument_list|(
name|Objects
operator|.
name|requireNonNull
argument_list|(
name|dbEntityName
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|String
name|toManyRelationshipName
parameter_list|(
name|DbRelationship
modifier|...
name|relationshipChain
parameter_list|)
block|{
name|DbRelationship
name|last
init|=
name|relationshipChain
index|[
name|relationshipChain
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|String
name|baseName
init|=
name|stemmed
argument_list|(
name|last
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
comment|// by default we use English rules here...
return|return
name|Noun
operator|.
name|pluralOf
argument_list|(
name|baseName
operator|.
name|toLowerCase
argument_list|()
argument_list|,
name|Locale
operator|.
name|ENGLISH
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|inflectorError
parameter_list|)
block|{
comment|//  seems that Inflector cannot be trusted. For instance, it
comment|// throws an exception when invoked for word "ADDRESS" (although
comment|// lower case works fine). To feel safe, we use superclass'
comment|// behavior if something's gone wrong
return|return
name|baseName
return|;
block|}
block|}
specifier|protected
name|String
name|toOneRelationshipName
parameter_list|(
name|DbRelationship
modifier|...
name|relationshipChain
parameter_list|)
block|{
name|DbRelationship
name|first
init|=
name|relationshipChain
index|[
literal|0
index|]
decl_stmt|;
name|DbRelationship
name|last
init|=
name|relationshipChain
index|[
name|relationshipChain
operator|.
name|length
operator|-
literal|1
index|]
decl_stmt|;
name|List
argument_list|<
name|DbJoin
argument_list|>
name|joins
init|=
name|first
operator|.
name|getJoins
argument_list|()
decl_stmt|;
if|if
condition|(
name|joins
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No joins for relationship. Can't generate a name"
argument_list|)
throw|;
block|}
name|DbJoin
name|join1
init|=
name|joins
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// TODO: multi-join relationships
comment|// return the name of the FK column sans ID
name|String
name|fkColName
init|=
name|join1
operator|.
name|getSourceName
argument_list|()
decl_stmt|;
if|if
condition|(
name|fkColName
operator|==
literal|null
condition|)
block|{
return|return
name|stemmed
argument_list|(
name|last
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
return|;
block|}
if|else if
condition|(
name|fkColName
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"_ID"
argument_list|)
operator|&&
name|fkColName
operator|.
name|length
argument_list|()
operator|>
literal|3
condition|)
block|{
return|return
name|fkColName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fkColName
operator|.
name|length
argument_list|()
operator|-
literal|3
argument_list|)
return|;
block|}
if|else if
condition|(
name|fkColName
operator|.
name|toUpperCase
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"ID"
argument_list|)
operator|&&
name|fkColName
operator|.
name|length
argument_list|()
operator|>
literal|2
condition|)
block|{
return|return
name|fkColName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fkColName
operator|.
name|length
argument_list|()
operator|-
literal|2
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|stemmed
argument_list|(
name|last
operator|.
name|getTargetEntityName
argument_list|()
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|String
name|objEntityName
parameter_list|(
name|DbEntity
name|dbEntity
parameter_list|)
block|{
name|String
name|baseName
init|=
name|stemmed
argument_list|(
name|dbEntity
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|Util
operator|.
name|underscoredToJava
argument_list|(
name|baseName
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|objAttributeName
parameter_list|(
name|DbAttribute
name|attr
parameter_list|)
block|{
return|return
name|Util
operator|.
name|underscoredToJava
argument_list|(
name|attr
operator|.
name|getName
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
end_class

end_unit

