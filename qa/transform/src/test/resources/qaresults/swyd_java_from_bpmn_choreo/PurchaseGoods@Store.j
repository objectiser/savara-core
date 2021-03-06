
/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

package org.savara.examples.store;

import org.jboss.examples.store.BuyRequest;
import org.jboss.examples.store.BuyConfirmed;
import org.jboss.examples.creditagency.CreditCheckRequest;
import org.jboss.examples.creditagency.CreditRating;
import org.jboss.examples.logistics.DeliveryRequest;
import org.jboss.examples.logistics.DeliveryConfirmed;
import org.savara.examples.creditagency.CustomerUnknownFault;

import java.util.logging.Logger;

@org.switchyard.component.bean.Service(Store.class)
public class StoreImpl implements Store {

    @javax.inject.Inject @org.switchyard.component.bean.Reference
    org.savara.examples.logistics.Logistics _logistics;

    @javax.inject.Inject @org.switchyard.component.bean.Reference
    org.savara.examples.creditagency.CreditAgency _creditAgency;

    private static final Logger LOG = Logger.getLogger(StoreImpl.class.getName());

    /* (non-Javadoc)
     * @see org.savara.examples.store.Store#buy(org.jboss.examples.store.BuyRequest  content )*
     */
    public org.jboss.examples.store.BuyConfirmed buy(org.jboss.examples.store.BuyRequest content) throws InsufficientCreditFault , AccountNotFoundFault    {
        BuyConfirmed ret=null;

        // TODO: Add code here to handle request (in variable 'content')

        try {
            // TODO: Add code here to initialize request
            CreditCheckRequest creditCheckReq=null;

            CreditRating creditCheckResult = _creditAgency.creditCheck(creditCheckReq);

            if (false) { // TODO: Set expression
                throw new InsufficientCreditFault();
            } else {
                // TODO: Add code here to initialize request
                DeliveryRequest deliveryReq=null;

                DeliveryConfirmed deliveryResult = _logistics.delivery(deliveryReq);

                // TODO: Add code here to return response
                // ret = ....;
            }

        } catch (CustomerUnknownFault customerUnknown) {
            throw new AccountNotFoundFault();
        }

        return (ret);
    }

}
