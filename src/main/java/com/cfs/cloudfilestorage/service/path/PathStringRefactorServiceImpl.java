package com.cfs.cloudfilestorage.service.path;

import com.cfs.cloudfilestorage.service.person.AuthorizedPersonService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;

@Service
public class PathStringRefactorServiceImpl implements PathStringRefactorService {

    private final AuthorizedPersonService authorizedPersonService;

    public PathStringRefactorServiceImpl(AuthorizedPersonService authorizedPersonService) {
        this.authorizedPersonService = authorizedPersonService;
    }

    @Override
    public String addBackSlash(String path) {
        try{
            if(path.endsWith("/"))
                return path;

            return path+"/";
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String removeBackSlash(String path) {
        try{
            if(path.endsWith("/"))
                return path.substring(0,path.length()-1);

            return path;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String getCleanName(String path) {
        try{
            var oPath = Paths.get(path);
            return oPath.getFileName().toString();
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String getOriginalName(String path) {
        try{
            var oPath = Paths.get(path);
            var result = oPath.getFileName().toString();

            if(path.endsWith("/"))
                result += "/";

            return result;
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String getParent(String path) {
        try{
            var oPath = Paths.get(path);
            return addBackSlash(oPath.getParent().toString());
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String addRoot(String path) {
        try{
            var root = getPersonRootFolder();
            return String.format("%s/%s", root, path);
        }catch (Exception e){
            return "";
        }
    }

    @Override
    public String subtraction(String minuend, String subtrahend) {
        try{
            var subtrahendIndex = minuend.indexOf(subtrahend);
            var beginIndex = subtrahendIndex + subtrahend.length();
            return minuend.substring(beginIndex);
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String replaceLast(String string, String toReplace, String replacement) {
        try {
            int pos = string.lastIndexOf(toReplace);
            if (pos > -1) {
                return string.substring(0, pos)
                        + replacement
                        + string.substring(pos + toReplace.length());
            } else {
                return string;
            }
        }catch (Exception ex){
            System.err.println(ex.getMessage());
            return "";
        }
    }

    @Override
    public String getPersonRootFolder(){
        var optPerson = authorizedPersonService.findPerson();

        if(optPerson.isEmpty()){
            throw new UsernameNotFoundException("person");
        }

        var person = optPerson.get();

        return String.format("user-%d-files", person.getId());
    }
}
