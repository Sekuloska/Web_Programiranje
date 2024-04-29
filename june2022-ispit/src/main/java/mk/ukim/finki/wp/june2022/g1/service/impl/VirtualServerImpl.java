package mk.ukim.finki.wp.june2022.g1.service.impl;

import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidUserIdException;
import mk.ukim.finki.wp.june2022.g1.model.exceptions.InvalidVirtualMachineIdException;
import mk.ukim.finki.wp.june2022.g1.repository.UserRepository;
import mk.ukim.finki.wp.june2022.g1.repository.VirtualServerRepository;
import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class VirtualServerImpl implements VirtualServerService {

    private final VirtualServerRepository virtualServerRepository;
    private final UserRepository userRepository;

    public VirtualServerImpl(VirtualServerRepository virtualServerRepository, UserRepository userRepository) {
        this.virtualServerRepository = virtualServerRepository;
        this.userRepository = userRepository;
    }

    /**
     * @return List of all entities in the database
     */
    public List<VirtualServer> listAll(){
        return this.virtualServerRepository.findAll();
    }

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     */
    public VirtualServer findById(Long id){
        return this.virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException::new);
    }

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidUserIdException when there is no user with the given id
     */
    public VirtualServer create(String name, String ipAddress, OSType osType, List<Long> owners, LocalDate launchDate){
        List<User> users=this.userRepository.findAllById(owners);
        VirtualServer virtualServer=new VirtualServer(name,ipAddress,osType,users,launchDate);

        return this.virtualServerRepository.save(virtualServer);
    }

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id          The id of the entity that is being edited
     * @return The entity that is updated.
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     * @throws InvalidUserIdException    when there is no user with the given id
     */
    public VirtualServer update(Long id, String name, String ipAddress, OSType osType, List<Long> owners){
        VirtualServer virtualServer=this.virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException::new);
        List<User> users=this.userRepository.findAllById(owners);

        virtualServer.setInstanceName(name);
        virtualServer.setIpAddress(ipAddress);
        virtualServer.setOSType(osType);
        virtualServer.setOwners(users);
        return this.virtualServerRepository.save(virtualServer);
    }

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidVirtualServerIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     */
    public VirtualServer delete(Long id){
        VirtualServer virtualServer=this.virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException::new);
        this.virtualServerRepository.delete(virtualServer);
        return virtualServer;
    }

    /**
     * Method that should mark as terminated the virtual server. If the id is invalid, it should throw InvalidVirtualMachineIdException.
     *
     * @param id
     * @return The entity that should be marked as terminated.
     * @throws InvalidVirtualMachineIdException when there is no entity with the given id
     */
    public VirtualServer markTerminated(Long id){
        VirtualServer virtualServer=this.virtualServerRepository.findById(id).orElseThrow(InvalidVirtualMachineIdException::new);
        virtualServer.setTerminated(true);
        return this.virtualServerRepository.save(virtualServer);
    }

    /**
     * The implementation of this method should use repository implementation for the filtering.
     * All arguments are nullable. When an argument is null, we should not filter by that attribute
     *
     * @return The entities that meet the filtering criteria
     */
   public List<VirtualServer> filter(Long ownerId, Integer activeMoreThanDays){

       //(launchDate <= (now - filtering days))
       if(ownerId==null && activeMoreThanDays==null)
       {
           return this.virtualServerRepository.findAll();
       }else if(ownerId!=null && activeMoreThanDays!=null){
           LocalDate launch=LocalDate.now().minusDays(activeMoreThanDays);
           User user=this.userRepository.findById(ownerId).orElseThrow(InvalidUserIdException::new);
           return this.virtualServerRepository.findAllByLaunchDateBeforeAndAndOwnersContaining(launch,user);
       }else if(ownerId!=null)
       {
           User user=this.userRepository.findById(ownerId).orElseThrow(InvalidUserIdException::new);
           return this.virtualServerRepository.findAllByOwnersContaining(user);
       }else if(activeMoreThanDays!=null){
           LocalDate launch=LocalDate.now().minusDays(activeMoreThanDays);
           return this.virtualServerRepository.findAllByLaunchDateBefore(launch);
       }
       return null;
   }
}
