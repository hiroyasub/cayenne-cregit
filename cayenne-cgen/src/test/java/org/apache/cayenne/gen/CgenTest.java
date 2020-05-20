begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cayenne
operator|.
name|gen
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
name|exp
operator|.
name|Expression
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
name|exp
operator|.
name|ExpressionFactory
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
name|*
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_class
specifier|public
class|class
name|CgenTest
block|{
specifier|protected
name|ClassGenerationAction
name|action
decl_stmt|;
specifier|protected
name|CgenConfiguration
name|cgenConfiguration
decl_stmt|;
specifier|protected
name|DataMap
name|dataMap
decl_stmt|;
specifier|protected
name|ObjEntity
name|objEntity
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|cgenConfiguration
operator|=
operator|new
name|CgenConfiguration
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|action
operator|=
operator|new
name|ClassGenerationAction
argument_list|(
name|cgenConfiguration
argument_list|)
expr_stmt|;
name|dataMap
operator|=
operator|new
name|DataMap
argument_list|()
expr_stmt|;
name|objEntity
operator|=
operator|new
name|ObjEntity
argument_list|()
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|dataMap
operator|=
literal|null
expr_stmt|;
name|objEntity
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSelectQuery
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|param
init|=
literal|"param"
decl_stmt|;
name|String
name|qualifierString
init|=
literal|"name = $"
operator|+
name|param
decl_stmt|;
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|ObjAttribute
name|attribute
init|=
operator|new
name|ObjAttribute
argument_list|(
literal|"name"
argument_list|)
decl_stmt|;
name|attribute
operator|.
name|setDbAttributePath
argument_list|(
literal|"testKey"
argument_list|)
expr_stmt|;
name|attribute
operator|.
name|setType
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|addAttribute
argument_list|(
name|attribute
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setClassName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|SelectQueryDescriptor
name|selectQueryDescriptor
init|=
operator|new
name|SelectQueryDescriptor
argument_list|()
decl_stmt|;
name|Expression
name|exp
init|=
name|ExpressionFactory
operator|.
name|exp
argument_list|(
name|qualifierString
argument_list|)
decl_stmt|;
name|selectQueryDescriptor
operator|.
name|setQualifier
argument_list|(
name|exp
argument_list|)
expr_stmt|;
name|selectQueryDescriptor
operator|.
name|setName
argument_list|(
literal|"select"
argument_list|)
expr_stmt|;
name|selectQueryDescriptor
operator|.
name|setRoot
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setName
argument_list|(
literal|"DataMapTest"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setDefaultPackage
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|descriptors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|descriptors
operator|.
name|add
argument_list|(
name|selectQueryDescriptor
argument_list|)
expr_stmt|;
name|DataMapArtifact
name|dataMapArtifact
init|=
operator|new
name|DataMapArtifact
argument_list|(
name|dataMap
argument_list|,
name|descriptors
argument_list|)
decl_stmt|;
name|execute
argument_list|(
name|dataMapArtifact
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSQLTemplate
parameter_list|()
throws|throws
name|Exception
block|{
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|objEntity
operator|.
name|setDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setClassName
argument_list|(
literal|"Test"
argument_list|)
expr_stmt|;
name|SQLTemplateDescriptor
name|sqlTemplateDescriptor
init|=
operator|new
name|SQLTemplateDescriptor
argument_list|()
decl_stmt|;
name|sqlTemplateDescriptor
operator|.
name|setSql
argument_list|(
literal|"SELECT * FROM table"
argument_list|)
expr_stmt|;
name|sqlTemplateDescriptor
operator|.
name|setRoot
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|sqlTemplateDescriptor
operator|.
name|setName
argument_list|(
literal|"select"
argument_list|)
expr_stmt|;
name|sqlTemplateDescriptor
operator|.
name|setRoot
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setName
argument_list|(
literal|"SQLTemplate"
argument_list|)
expr_stmt|;
name|dataMap
operator|.
name|setDefaultPackage
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
name|Collection
argument_list|<
name|QueryDescriptor
argument_list|>
name|descriptors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|descriptors
operator|.
name|add
argument_list|(
name|sqlTemplateDescriptor
argument_list|)
expr_stmt|;
name|DataMapArtifact
name|dataMapArtifact
init|=
operator|new
name|DataMapArtifact
argument_list|(
name|dataMap
argument_list|,
name|descriptors
argument_list|)
decl_stmt|;
name|execute
argument_list|(
name|dataMapArtifact
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGenClass
parameter_list|()
throws|throws
name|Exception
block|{
name|dataMap
operator|.
name|setName
argument_list|(
literal|"EntityTest"
argument_list|)
expr_stmt|;
name|DbEntity
name|dbEntity
init|=
operator|new
name|DbEntity
argument_list|()
decl_stmt|;
name|dbEntity
operator|.
name|setName
argument_list|(
literal|"EntityTest"
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setDbEntity
argument_list|(
name|dbEntity
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setClassName
argument_list|(
literal|"test.EntityTest"
argument_list|)
expr_stmt|;
name|objEntity
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|EntityArtifact
name|entityArtifact
init|=
operator|new
name|EntityArtifact
argument_list|(
name|objEntity
argument_list|)
decl_stmt|;
name|execute
argument_list|(
name|entityArtifact
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|execute
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|Exception
block|{
name|cgenConfiguration
operator|.
name|addArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setRelPath
argument_list|(
literal|"src/test/resources"
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|loadEntity
argument_list|(
name|objEntity
argument_list|)
expr_stmt|;
name|cgenConfiguration
operator|.
name|setDataMap
argument_list|(
name|dataMap
argument_list|)
expr_stmt|;
name|action
operator|.
name|setUtilsFactory
argument_list|(
operator|new
name|DefaultToolsUtilsFactory
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|execute
argument_list|()
expr_stmt|;
name|fileComparison
argument_list|(
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|fileComparison
argument_list|(
literal|"auto/_"
operator|+
name|dataMap
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|rmdir
argument_list|(
operator|new
name|File
argument_list|(
name|cgenConfiguration
operator|.
name|getRelPath
argument_list|()
operator|+
literal|"/test"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|fileComparison
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
try|try
block|{
name|FileReader
name|fileReader1
init|=
operator|new
name|FileReader
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/templateTest/"
operator|+
name|fileName
operator|+
literal|".java"
argument_list|)
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader1
init|=
operator|new
name|BufferedReader
argument_list|(
name|fileReader1
argument_list|)
decl_stmt|;
name|String
name|lineFile1
decl_stmt|;
name|String
name|string1
init|=
literal|""
decl_stmt|;
name|FileReader
name|fileReader2
init|=
operator|new
name|FileReader
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/resources/test/"
operator|+
name|fileName
operator|+
literal|".java"
argument_list|)
argument_list|)
decl_stmt|;
name|BufferedReader
name|reader2
init|=
operator|new
name|BufferedReader
argument_list|(
name|fileReader2
argument_list|)
decl_stmt|;
name|String
name|lineFile2
decl_stmt|;
name|String
name|string2
init|=
literal|""
decl_stmt|;
while|while
condition|(
operator|(
name|lineFile1
operator|=
name|reader1
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
operator|&&
operator|(
name|lineFile2
operator|=
name|reader2
operator|.
name|readLine
argument_list|()
operator|)
operator|!=
literal|null
condition|)
block|{
name|string1
operator|+=
name|lineFile1
expr_stmt|;
name|string2
operator|+=
name|lineFile2
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|string1
argument_list|,
name|string2
argument_list|)
expr_stmt|;
name|reader1
operator|.
name|close
argument_list|()
expr_stmt|;
name|reader2
operator|.
name|close
argument_list|()
expr_stmt|;
name|fileReader1
operator|.
name|close
argument_list|()
expr_stmt|;
name|fileReader2
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|rmdir
parameter_list|(
name|File
name|file
parameter_list|)
block|{
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
return|return;
if|if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
for|for
control|(
name|File
name|f
range|:
name|file
operator|.
name|listFiles
argument_list|()
control|)
block|{
name|rmdir
argument_list|(
name|f
argument_list|)
expr_stmt|;
block|}
block|}
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

