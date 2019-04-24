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
name|stubs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Objects
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
name|dbsync
operator|.
name|naming
operator|.
name|DbEntityNameStemmer
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
name|dbsync
operator|.
name|naming
operator|.
name|NoStemStemmer
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
name|dbsync
operator|.
name|naming
operator|.
name|ObjectNameGenerator
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

begin_class
specifier|public
class|class
name|CustomObjectNameGenerator
implements|implements
name|ObjectNameGenerator
block|{
specifier|private
name|DbEntityNameStemmer
name|dbEntityNameStemmer
decl_stmt|;
specifier|public
name|CustomObjectNameGenerator
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
name|CustomObjectNameGenerator
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
return|return
literal|null
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
literal|"Custom"
operator|+
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
literal|"custom_"
operator|+
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

