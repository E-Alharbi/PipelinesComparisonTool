# Proper string handling in python of objects returned from clipper python
# benefits from import of the hklfile binding...I fI understood SWIG I might know why
#import ccp4mg
#import clipper
import re
import sys
import os
#sys.path.append(os.path.join(os.environ["CCP4"],"share","ccp4i2"))
#sys.path.append("/Applications/ccp4-7.0/lib/py2")
#sys.path.append(os.environ["CCP4"])
#sys.path.append("/Applications/ccp4-7.0/share/python/CCP4Dispatchers")
#sys.path.append("/Applications/ccp4-7.0/lib/py2")
#sys.path.append("/Users/emadalharbi/devel")
sys.path.append("ccp4i2Core")
#sys.path.append("/Applications/ccp4-7.0/share/ccp4i2")
import ccp4mg
import clipper
print "#########PluginUtils#######"
print sys.path
from core import CCP4Modules
from core.CCP4ErrorHandling import CException
from core import CCP4XtalData

class Runner:
  def __init__(self, dbFile=None):
    from utils.startup import startProjectsManager
    self.pm = startProjectsManager(dbFileName=dbFile)

  def jobForTaskInProjectName(self, projectName=None, projectPath=None, taskName=None):
    from dbapi import CCP4DbUtils
    if projectPath is None: projectPath = os.path.join(os.getcwd(), projectName)
    # Create a project
    try:
        #First try as if the projectPath does not yet exist
        try:
            os.mkdir(projectPath)
        except:
            print "Project directory already exists", projectPath
        projectId =  self.pm.createProject(projectName=projectName,projectPath=projectPath)
    except CException as cExcept:
        print cExcept
        projectId =  self.pm.db().getProjectId(projectName)
    
    # Create instance of COpenJob which will create/run a job for us
    cOpenJob = CCP4DbUtils.COpenJob(projectId=projectId)
    cOpenJob.createJob(taskName=taskName)
    
    return cOpenJob

  def setRunning(self, cOpenJob):
    rv = cOpenJob.saveParams()
    from core.CCP4ErrorHandling import SEVERITY_WARNING
    if rv.maxSeverity()>SEVERITY_WARNING: return rv
    jobProcess = self.importGleanAndRun(cOpenJob)
    return jobProcess

  def jobDirectory(self, cOpenJob):
    return self.pm.jobDirectory(jobId=cOpenJob.jobId)

  def importGleanAndRun(self, cOpenJob):
    ifImportFile,errors = self.pm.importFiles(jobId=cOpenJob.jobId, container=cOpenJob.container)
    #print 'COpenJob.runJob',ifImportFile,errors
    
    #Record input files in database
    from dbapi import CCP4DbApi
    self.pm.db().gleanJobFiles(jobId=cOpenJob.__dict__['jobId'],
                               container=cOpenJob.__dict__['container'],
                               projectId=cOpenJob.__dict__['_projectId'],
                               roleList=[CCP4DbApi.FILE_ROLE_IN])
                       
    from core.CCP4Modules import JOBCONTROLLER
    jobController = JOBCONTROLLER()
    jobController.setDbFile(self.pm.db()._fileName)
    jobController._diagnostic=True
    #jobController.USE_QPROCESS=False
    jobProcess = JOBCONTROLLER().runTask(jobId=cOpenJob.jobId)
    return jobProcess

def availableNameBasedOn(filePath):
    if not os.path.exists(filePath): return filePath
    if "." in filePath:
        #Clumsy thing to deal with double dotted extensions like .scene.xml
        basePath = filePath.split(".")[0]
        extension = "."+".".join(filePath.split(".")[1:])
    else:
        basePath = filePath
        extension = ""
    counter = 1
    while os.path.exists(basePath+"_"+str(counter)+extension):
        counter += 1
    return basePath+"_"+str(counter)+extension

def smartSplitMTZ(inputFilePath=None, inputColumnPath=None, objectPath=None, intoDirectory=None):
    if inputFilePath is None: raise Exception("smartSplitMTZ Exception:", "Must provide an input file")
    if not os.path.isfile(inputFilePath): raise Exception("smartSplitMTZ Exception:", "inputFile must exist"+str(inputFilePath))
    if inputColumnPath is None: raise Exception("smartSplitMTZ Exception:", "Must provide an input columnPath e.g. '/*/*/[F,SIGFP]'")
    if objectPath is not None and intoDirectory is not None: raise Exception("smartSplitMTZ Exception:", "Provide either full output path for file, or name of directory where file should be placed")
    if objectPath is  None and intoDirectory is  None: raise Exception("smartSplitMTZ Exception:", "Provide either full output path for file, or name of directory where file should be placed")
                                                                               

    mtz_file = clipper.CCP4MTZfile()
    hkl_info = clipper.HKL_info()
    mtz_file.open_read (inputFilePath)
    mtz_file.import_hkl_info ( hkl_info )
    xtal = clipper.MTZcrystal()
    mtz_file.import_crystal( xtal, inputColumnPath )
    dataset=clipper.MTZdataset()
    mtz_file.import_dataset( dataset, inputColumnPath )
    providedColumnPaths = mtz_file.column_paths()
    
    selectedColumnLabelsExp=re.compile(r"^/(?P<XtalName>[A-Za-z0-9_. -+\*,]+)/(?P<DatasetName>[A-Za-z0-9_. -+\*,]+)/\[(?P<Columns>[A-Za-z0-9_. -+\*,]+)\]")
    columnsMatch=selectedColumnLabelsExp.search(inputColumnPath)
    selectedColumnLabelExp=re.compile(r"^/(?P<XtalName>[A-Za-z0-9_. -+\*,]+)/(?P<DatasetName>[A-Za-z0-9_. -+\*,]+)/(?P<Column>[A-Za-z0-9_. -+\*,]+)")
    columnMatch=selectedColumnLabelExp.search(inputColumnPath)
    if columnsMatch is not None:
        selectedColumnPaths  =["/{}/{}/{}".format(columnsMatch.group("XtalName"),columnsMatch.group("DatasetName"),column) for column in columnsMatch.group("Columns").split(",") ]
    elif columnMatch is not None:
        selectedColumnPaths  =["/{}/{}/{}".format(columnMatch.group("XtalName"),columnMatch.group("DatasetName"),columnMatch.group("Column"))]

    typeSignature = ""
    for selectedColumnPath in selectedColumnPaths:
        selectedColumnMatch = selectedColumnLabelExp.search(selectedColumnPath)
        for providedColumnPath in providedColumnPaths:
            #Generating clipper String and then calling str to deal with
            #Known unpredictable bug in clipper-python
            try:
              columnName, columnType = str(clipper.String(providedColumnPath)).split(" ")
            except NotImplementedError as err:
              columnName, columnType = str(providedColumnPath).split(" ")
            parsedColumnMatch = selectedColumnLabelExp.search(columnName)
            if ((selectedColumnMatch.group("XtalName") == "*" or selectedColumnMatch.group("XtalName") == parsedColumnMatch.group("XtalName")) and
                (selectedColumnMatch.group("DatasetName") == "*" or selectedColumnMatch.group("DatasetName") == parsedColumnMatch.group("DatasetName")) and
                selectedColumnMatch.group("Column") == parsedColumnMatch.group("Column")):
                typeSignature += columnType
                break

    if typeSignature == "FQ":
        extractedData = clipper.HKL_data_F_sigF_float(hkl_info)
        cls = CCP4XtalData.CObsDataFile
        contentType = 4
    if typeSignature == "JQ":
        extractedData = clipper.HKL_data_I_sigI_float(hkl_info)
        cls = CCP4XtalData.CObsDataFile
        contentType = 3
    if typeSignature == "GLGL" or typeSignature == "FQFQ":
        extractedData = clipper.HKL_data_F_sigF_ano_float(hkl_info)
        cls = CCP4XtalData.CObsDataFile
        contentType = 2
    if typeSignature == "KMKM"  or typeSignature == "JQJQ":
        extractedData = clipper.HKL_data_I_sigI_ano_float(hkl_info)
        cls = CCP4XtalData.CObsDataFile
        contentType = 1
    elif typeSignature == "AAAA":
        extractedData = clipper.HKL_data_ABCD_float(hkl_info)
        cls = CCP4XtalData.CPhsDataFile
        contentType = 1
    elif typeSignature == "PW":
        extractedData = clipper.HKL_data_Phi_fom_float(hkl_info)
        cls = CCP4XtalData.CPhsDataFile
        contentType = 2
    elif typeSignature == "I":
        extractedData = clipper.HKL_data_Flag(hkl_info)
        cls = CCP4XtalData.CFreeRDataFile
        contentType = 1
    outputColumnPath = "[{}]".format(','.join(getattr(cls, "CONTENT_SIGNATURE_LIST")[contentType-1]))

    mtz_file.import_hkl_data( extractedData, inputColumnPath )
    mtz_file.close_read()

    if intoDirectory is not None:
        firstGuess = os.path.join(intoDirectory,typeSignature+'_ColumnsFrom_'+os.path.split(inputFilePath)[1])
        objectPath = availableNameBasedOn(firstGuess)

    mtzout = clipper.CCP4MTZfile()
    mtzout.open_write( objectPath )
    mtzout.export_hkl_info( hkl_info )
    outputColumnPath = "/{}/{}/{}".format(str(xtal.crystal_name()), str(dataset.dataset_name()), outputColumnPath )
    mtzout.export_crystal( xtal, outputColumnPath )
    mtzout.export_dataset( dataset, outputColumnPath )
    mtzout.export_hkl_data( extractedData, outputColumnPath )
    mtzout.close_write()

    return objectPath

def makeASUContentFile(inputFileDicts, projectName=None, objectPath=None, intoDirectory=None):
    from core import CCP4File
    from lxml import etree
    
    if intoDirectory is not None:
        firstGuess = os.path.join(intoDirectory, '_SequencesFrom_'+os.path.split(inputFileDicts[0]["seqFile"])[1])
        objectPath = availableNameBasedOn(firstGuess)
    
    xmlFileObject = CCP4File.CI2XmlDataFile(objectPath)
    xmlFileObject.header.setCurrent()
    xmlFileObject.header.function.set('ASUCONTENT')
    xmlFileObject.header.projectName.set(projectName)
    baseRoot=etree.Element("root")
    sequenceListRoot=etree.SubElement(baseRoot, 'seqList')
    
    for inputFileDict in inputFileDicts:
        from core.CCP4ModelData import CAsuContentSeq
        cAsuContentSeq = CAsuContentSeq()
        cAsuContentSeq.nCopies = 1
        inputSeqPath = inputFileDict.get("seqFile",None)
        contentsOfSeqFile = open(inputSeqPath).read()
        linesToSkip = inputFileDict.get("skipLines", 0)
        seqByLine = contentsOfSeqFile.split('\n')[linesToSkip:]
        seqByLineTrimmed = [sequenceLine.strip() for sequenceLine in seqByLine]
        cAsuContentSeq.sequence = "".join(seqByLineTrimmed)
        cAsuContentSeq.name = 'A'
        sequenceListRoot.append(cAsuContentSeq.getEtree())
    xmlFileObject.saveFile(baseRoot)

    return objectPath

# ==========================================================================================================
if __name__ == "__main__":
    makeMiniMTZ("pb_jcsg_sets/1vjf-parrot.mtz", "/*/*/[FP,SIGFP]", "testFs.mtz")
    makeMiniMTZ("pb_jcsg_sets/1vjf-parrot.mtz", "/*/*/[parrot.ABCD.A,parrot.ABCD.B,parrot.ABCD.C,parrot.ABCD.D]", "testHLAs.mtz")
    makeMiniMTZ("pb_jcsg_sets/1vjf-parrot.mtz", "/*/*/[FreeR_flag]", "testFREER.mtz")


