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

begin_comment
comment|/**  * @since 4.0  */
end_comment

begin_class
specifier|public
class|class
name|NoStemStemmer
implements|implements
name|DbEntityNameStemmer
block|{
specifier|private
specifier|static
specifier|final
name|DbEntityNameStemmer
name|INSTANCE
init|=
operator|new
name|NoStemStemmer
argument_list|()
decl_stmt|;
specifier|public
specifier|static
name|DbEntityNameStemmer
name|getInstance
parameter_list|()
block|{
return|return
name|INSTANCE
return|;
block|}
specifier|private
name|NoStemStemmer
parameter_list|()
block|{
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
return|return
name|dbEntityName
return|;
block|}
block|}
end_class

end_unit

