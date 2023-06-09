begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*****************************************************************  *   Licensed to the Apache Software Foundation (ASF) under one  *  or more contributor license agreements.  See the NOTICE file  *  distributed with this work for additional information  *  regarding copyright ownership.  The ASF licenses this file  *  to you under the Apache License, Version 2.0 (the  *  "License"); you may not use this file except in compliance  *  with the License.  You may obtain a copy of the License at  *  *    https://www.apache.org/licenses/LICENSE-2.0  *  *  Unless required by applicable law or agreed to in writing,  *  software distributed under the License is distributed on an  *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  *  KIND, either express or implied.  See the License for the  *  specific language governing permissions and limitations  *  under the License.  ****************************************************************/
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
name|translator
operator|.
name|select
package|;
end_package

begin_comment
comment|/**  * @since 4.2  */
end_comment

begin_class
class|class
name|PathComponents
block|{
specifier|private
specifier|final
name|String
name|path
decl_stmt|;
specifier|private
name|String
name|parent
decl_stmt|;
specifier|private
name|String
index|[]
name|pathComponents
decl_stmt|;
name|PathComponents
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|initPath
argument_list|()
expr_stmt|;
block|}
name|int
name|size
parameter_list|()
block|{
return|return
name|pathComponents
operator|.
name|length
return|;
block|}
name|String
name|getLast
parameter_list|()
block|{
return|return
name|pathComponents
index|[
name|pathComponents
operator|.
name|length
operator|-
literal|1
index|]
return|;
block|}
name|String
name|getParent
parameter_list|()
block|{
return|return
name|parent
return|;
block|}
name|String
index|[]
name|getAll
parameter_list|()
block|{
return|return
name|pathComponents
return|;
block|}
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|private
name|void
name|initPath
parameter_list|()
block|{
name|int
name|count
init|=
literal|1
decl_stmt|;
name|int
name|last
init|=
literal|0
decl_stmt|;
comment|// quick scan to check for path separator in path
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|path
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|path
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'.'
condition|)
block|{
name|count
operator|++
expr_stmt|;
name|last
operator|=
name|i
expr_stmt|;
block|}
block|}
comment|// fast path, simple path
if|if
condition|(
name|count
operator|==
literal|1
condition|)
block|{
name|pathComponents
operator|=
operator|new
name|String
index|[]
block|{
name|path
block|}
expr_stmt|;
name|parent
operator|=
literal|""
expr_stmt|;
return|return;
block|}
comment|// two parts path, can be fast too
name|pathComponents
operator|=
operator|new
name|String
index|[
name|count
index|]
expr_stmt|;
name|parent
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|last
argument_list|)
expr_stmt|;
if|if
condition|(
name|count
operator|==
literal|2
condition|)
block|{
name|pathComponents
index|[
literal|0
index|]
operator|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|last
argument_list|)
expr_stmt|;
name|pathComponents
index|[
literal|1
index|]
operator|=
name|path
operator|.
name|substring
argument_list|(
name|last
operator|+
literal|1
argument_list|)
expr_stmt|;
return|return;
block|}
comment|// additional full scan
name|last
operator|=
literal|0
expr_stmt|;
name|int
name|idx
init|=
literal|0
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|path
operator|.
name|length
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|path
operator|.
name|charAt
argument_list|(
name|i
argument_list|)
operator|==
literal|'.'
condition|)
block|{
name|pathComponents
index|[
name|idx
operator|++
index|]
operator|=
name|path
operator|.
name|substring
argument_list|(
name|last
argument_list|,
name|i
argument_list|)
expr_stmt|;
name|last
operator|=
name|i
operator|+
literal|1
expr_stmt|;
block|}
block|}
name|pathComponents
index|[
name|idx
index|]
operator|=
name|path
operator|.
name|substring
argument_list|(
name|last
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

