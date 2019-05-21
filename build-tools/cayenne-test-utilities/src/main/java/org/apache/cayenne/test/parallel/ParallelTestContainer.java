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
name|test
operator|.
name|parallel
package|;
end_package

begin_comment
comment|/**  * Helper class allowing unit tests to wait till a code in question executes in  * a separate thread. There is still some element of uncertainty remains, since  * this implementation simply tries to give other threads enough time to  * execute, instead of watching for threads activity.  *   *<p>  * Note that result sampling is done every 300 ms., so if the test succeeds  * earlier, test case wouldn't have to wait for the whole time period specified  * by timeout.  *</p>  *   */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|ParallelTestContainer
block|{
specifier|protected
specifier|abstract
name|void
name|assertResult
parameter_list|()
throws|throws
name|Exception
function_decl|;
specifier|public
name|void
name|runTest
parameter_list|(
name|long
name|timeoutMs
parameter_list|)
throws|throws
name|Exception
block|{
name|long
name|checkEveryXMs
decl_stmt|;
name|int
name|maxMumberOfChecks
decl_stmt|;
if|if
condition|(
name|timeoutMs
operator|<
literal|300
condition|)
block|{
name|maxMumberOfChecks
operator|=
literal|1
expr_stmt|;
name|checkEveryXMs
operator|=
name|timeoutMs
expr_stmt|;
block|}
else|else
block|{
name|maxMumberOfChecks
operator|=
name|Math
operator|.
name|round
argument_list|(
name|timeoutMs
operator|/
literal|300.00f
argument_list|)
expr_stmt|;
name|checkEveryXMs
operator|=
literal|300
expr_stmt|;
block|}
comment|// TODO: for things asserting that a certain event DID NOT happen
comment|// we need a better implementation, that should probably sleep for
comment|// the whole timeout interval, since otherwise we may have a false
comment|// positive (i.e. assertion succeeded not because a certain thing did
comment|// not happen, but rather cause it happened after the assertion was
comment|// run).
comment|// for now lets wait for at least one time slice to decrease
comment|// the possibility of false positives
name|Thread
operator|.
name|sleep
argument_list|(
name|checkEveryXMs
argument_list|)
expr_stmt|;
name|maxMumberOfChecks
operator|--
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|maxMumberOfChecks
condition|;
name|i
operator|++
control|)
block|{
try|try
block|{
name|assertResult
argument_list|()
expr_stmt|;
comment|// success... return immediately
return|return;
block|}
catch|catch
parameter_list|(
name|Throwable
name|th
parameter_list|)
block|{
comment|// wait some more
name|Thread
operator|.
name|sleep
argument_list|(
name|checkEveryXMs
argument_list|)
expr_stmt|;
block|}
block|}
comment|// if it throws, it throws...
name|assertResult
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

