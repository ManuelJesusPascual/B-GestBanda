package com.mjpascual.gestbanda.services;

import com.mjpascual.gestbanda.dto.users.UsersResponse;
import com.mjpascual.gestbanda.entities.Users;
import com.mjpascual.gestbanda.entities.UsersBand;
import com.mjpascual.gestbanda.exceptions.ErrorCodes;
import com.mjpascual.gestbanda.exceptions.GestBandaException;
import com.mjpascual.gestbanda.repositories.UsersBandRepository;
import com.mjpascual.gestbanda.repositories.UsersRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.mjpascual.gestbanda.services.UsersBandService.ROLE_DIRECTOR;
import static com.mjpascual.gestbanda.services.UsersBandService.ROLE_MEMBER;
import static com.mjpascual.gestbanda.services.UsersBandService.ROLE_VOICE_LEADER;
import static java.util.Objects.isNull;

/**
 * Decides what the user of the current request can do. The user always comes from the JWT,
 * never from the request body.
 */
@Service
@Transactional
public class BandAccessService {

    private final UsersRepository usersRepository;
    private final UsersBandRepository usersBandRepository;

    public BandAccessService(UsersRepository usersRepository, UsersBandRepository usersBandRepository) {
        this.usersRepository = usersRepository;
        this.usersBandRepository = usersBandRepository;
    }

    /**
     * @return the user of the current request, taken from the JWT.
     */
    public UsersResponse currentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Users user = isNull(auth) ? null : usersRepository.findByEmail(auth.getName());
        if (isNull(user)) {
            throw new GestBandaException(new Exception(), "Usuario no autenticado", ErrorCodes.UNAUTHORIZED);
        }
        return UsersResponse.from(user);
    }

    /**
     * Allows the request only if the current user is the given user.
     */
    public void requireSelf(UUID userId) {
        if (!currentUser().getId().equals(userId)) {
            throw forbidden();
        }
    }

    /**
     * Allows the request only if the current user is the given user or shares a band with them.
     */
    public void requireSelfOrSharedBand(UUID userId) {
        UUID me = currentUser().getId();
        if (!me.equals(userId) && !usersBandRepository.shareABand(me, userId)) {
            throw forbidden();
        }
    }

    /**
     * @return the current user's membership in the band.
     */
    public UsersBand requireMember(UUID bandId) {
        UsersBand membership = usersBandRepository.findByUserBandID(currentUser().getId(), bandId);
        if (isNull(membership)) {
            throw forbidden();
        }
        return membership;
    }

    /**
     * Allows the request only if the current user is the director of the band.
     */
    public UsersBand requireDirector(UUID bandId) {
        UsersBand membership = requireMember(bandId);
        if (!hasRole(membership, ROLE_DIRECTOR)) {
            throw forbidden();
        }
        return membership;
    }

    /**
     * Allows {@code actor} to change or remove {@code target}: the director can manage anyone,
     * a voice leader can manage the COMPONENTE members of their own voice.
     */
    public void requireCanManage(UsersBand actor, UsersBand target) {
        if (hasRole(actor, ROLE_DIRECTOR)) {
            return;
        }
        boolean leaderOfTargetVoice = hasRole(actor, ROLE_VOICE_LEADER)
                && hasRole(target, ROLE_MEMBER)
                && actor.getVoice_id().getId().equals(target.getVoice_id().getId());
        if (!leaderOfTargetVoice) {
            throw forbidden();
        }
    }

    public static boolean hasRole(UsersBand membership, String role) {
        return role.equals(membership.getRol_id().getName());
    }

    private static GestBandaException forbidden() {
        return new GestBandaException(new Exception(), "No tienes permiso para realizar esta acción", ErrorCodes.FORBIDDEN);
    }
}
