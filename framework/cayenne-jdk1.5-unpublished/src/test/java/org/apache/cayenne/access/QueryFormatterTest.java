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
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_class
specifier|public
class|class
name|QueryFormatterTest
extends|extends
name|TestCase
block|{
comment|/**      * Check if we have lose some words after formatting.      */
specifier|public
name|void
name|testQueryFormattingNotLosingWords
parameter_list|()
block|{
name|String
name|notFormattedQuery
init|=
literal|"declare   @Amount MONEY,  @BatchBrief BRIEFNAME, "
operator|+
literal|" @BatchID IDENTIFIER,  @BranchBrief ACCNUMBER,  @BranchID IDENTIFIER, "
operator|+
literal|" @Comment COMMENT,  @CurrencyID IDENTIFIER,  @CurrencyNumber BRIEFNAME, "
operator|+
literal|" @DateEnd OPERDAY,  @DateOpen OPERDAY,  @DateStart OPERDAY, "
operator|+
literal|" @DepositProductBrief BRIEFNAME,  @DepositProductID IDENTIFIER,"
operator|+
literal|"  @GiverBrief USERNAME,  @GiverID IDENTIFIER,  @InterestRateValue FLOAT, "
operator|+
literal|" @Number NUMBER20,  @OwnerAgentBrief USERNAME,  @OwnerAgentID IDENTIFIER,"
operator|+
literal|"  @OwnerBrief USERNAME,  @OwnerID IDENTIFIER,  @TermDay INT_KEY, "
operator|+
literal|" @TermDepositID IDENTIFIER,  @TermID IDENTIFIER,  @TermMonth INT_KEY, "
operator|+
literal|" @UserFIOBrief USERNAME,  @UserID IDENTIFIER,  "
operator|+
literal|"@ReturnCode IDENTIFIER select  @Amount = ?, "
operator|+
literal|"   @BatchBrief = ?,    @BatchID = ?,    @BranchBrief = ?,   "
operator|+
literal|" @BranchID = ?,    @Comment = ?,    @CurrencyID = ?,    @CurrencyNumber = ?, "
operator|+
literal|"   @DateEnd = ?,    @DateOpen = ?,    @DateStart = ?,    @DepositProductBrief = ?,  "
operator|+
literal|"  @DepositProductID = ?,    @GiverBrief = ?,    @GiverID = ?,    @InterestRateValue = ?, "
operator|+
literal|"   @Number = ?,    @OwnerAgentBrief = ?,    @OwnerAgentID = ?,    @OwnerBrief = ?, "
operator|+
literal|"   @OwnerID = ?,    @TermDay = ?,    @TermID = ?,    @TermMonth = ?,    @UserFIOBrief = ?,"
operator|+
literal|"    @UserID = ?,    @ReturnCode = -1   exec @ReturnCode = MY_STORED_PROCEDURE  @Amount "
operator|+
literal|"  =  @Amount  ,  @BatchBrief   =  @BatchBrief  ,  @BatchID   =  @BatchID  ,  @BranchBrief "
operator|+
literal|"  =  @BranchBrief  ,  @BranchID   =  @BranchID  ,  @Comment   =  @Comment  ,  @CurrencyID "
operator|+
literal|"  =  @CurrencyID  ,  @CurrencyNumber   =  @CurrencyNumber  ,  @DateEnd   =  @DateEnd  ,"
operator|+
literal|"  @DateOpen   =  @DateOpen  ,  @DateStart   =  @DateStart  ,  @DepositProductBrief   ="
operator|+
literal|"  @DepositProductBrief  ,  @DepositProductID   =  @DepositProductID  ,  @GiverBrief   = "
operator|+
literal|" @GiverBrief  ,  @GiverID   =  @GiverID  ,  @InterestRateValue   =  @InterestRateValue "
operator|+
literal|" ,  @Number   =  @Number  ,  @OwnerAgentBrief   =  @OwnerAgentBrief  ,  @OwnerAgentID "
operator|+
literal|"  =  @OwnerAgentID  ,  @OwnerBrief   =  @OwnerBrief  ,  @OwnerID   =  @OwnerID  , "
operator|+
literal|" @TermDay   =  @TermDay  ,  @TermDepositID   =  @TermDepositID out  ,  @TermID   = "
operator|+
literal|" @TermID  ,  @TermMonth   =  @TermMonth  ,  @UserFIOBrief   =  @UserFIOBrief  ,"
operator|+
literal|"  @UserID   =  @UserID  select  @TermDepositID AS TermDepositID, "
operator|+
literal|" @ReturnCode AS ReturnCode"
decl_stmt|;
name|String
index|[]
name|wordsNFQ
init|=
name|notFormattedQuery
operator|.
name|split
argument_list|(
literal|"\\s+"
argument_list|)
decl_stmt|;
name|String
name|formattedQuery
init|=
name|QueryFormatter
operator|.
name|formatQuery
argument_list|(
name|notFormattedQuery
argument_list|)
decl_stmt|;
name|String
index|[]
name|wordsFQ
init|=
name|formattedQuery
operator|.
name|split
argument_list|(
literal|"\\s+"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|wordsNFQ
operator|.
name|length
argument_list|,
name|wordsFQ
operator|.
name|length
argument_list|)
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
name|wordsNFQ
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertTrue
argument_list|(
name|formattedQuery
operator|.
name|contains
argument_list|(
name|wordsNFQ
index|[
name|i
index|]
argument_list|)
operator|||
name|formattedQuery
operator|.
name|contains
argument_list|(
name|wordsNFQ
index|[
name|i
index|]
operator|.
name|toUpperCase
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|formattedQuery
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

