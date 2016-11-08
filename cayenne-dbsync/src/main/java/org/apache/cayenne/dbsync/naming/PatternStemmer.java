begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  *    Licensed to the Apache Software Foundation (ASF) under one  *    or more contributor license agreements.  See the NOTICE file  *    distributed with this work for additional information  *    regarding copyright ownership.  The ASF licenses this file  *    to you under the Apache License, Version 2.0 (the  *    "License"); you may not use this file except in compliance  *    with the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  *    Unless required by applicable law or agreed to in writing,  *    software distributed under the License is distributed on an  *    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *    KIND, either express or implied.  See the License for the  *    specific language governing permissions and limitations  *    under the License.  */
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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|PatternStemmer
implements|implements
name|DbEntityNameStemmer
block|{
specifier|private
name|String
name|stripPattern
decl_stmt|;
specifier|public
name|PatternStemmer
parameter_list|(
name|String
name|stripPattern
parameter_list|)
block|{
name|this
operator|.
name|stripPattern
operator|=
name|stripPattern
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|stem
parameter_list|(
name|String
name|dbEntityName
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"TODO"
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

